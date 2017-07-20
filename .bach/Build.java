import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static java.io.File.pathSeparator;
import static java.util.stream.Collectors.joining;

class Build {

    public static void main(String... args) throws Exception {
        new Build().build();
    }

    private Bach bach = new Bach();

    private void build() throws IOException {
        System.out.printf("Building [%s] using JShell & Bach%n", bach.project.name);
        bach.project.pathTarget = Paths.get("out", "bach");

        resolve(Paths.get(".idea", "libraries"));

        compileModule("com.mechanitis.demo.sense.service");
        compileModule("com.mechanitis.demo.sense.flow");
        compileModule("com.mechanitis.demo.sense.mood");
        compileModule("com.mechanitis.demo.sense.user");
        compileModule("com.mechanitis.demo.sense.twitter");
        compileModule("com.mechanitis.demo.sense.client");

        test(generateClassPath().stream().map(Object::toString).collect(joining(pathSeparator)));
    }

    private void compileModule(String name) throws IOException {
        // main modules (module-path)
        Path sourceMain = bach.root.resolve(name + "/src").normalize();
        Path targetMain = bach.project.resolveTargetMods().resolve(name);
        Files.createDirectories(targetMain);
        bach.javac(
                javacOptions -> {
                    javacOptions.destinationPath = targetMain;
                    javacOptions.classSourcePaths = List.of(sourceMain);
                    javacOptions.modulePaths = List.of(bach.project.resolveTargetMods(),
                                                       bach.project.resolveAuxResolved());
                    return javacOptions;
                });
        // tests (class-path)
        Path sourceTest = bach.root.resolve(name + "/test").normalize();
        Path targetTest = bach.project.resolveTarget().resolve("test").resolve(name);
        if (Files.notExists(sourceTest)) {
            return;
        }
        Files.createDirectories(targetTest);

        bach.javac(
                javacOptions -> {
                    javacOptions.destinationPath = targetTest;
                    javacOptions.classSourcePaths = List.of(sourceTest);
                    javacOptions.classPaths = generateClassPath();
                    return javacOptions;
                });
    }

    private List<Path> generateClassPath() {
        List<Path> classPath = new ArrayList<>();
        try {
            Files.walk(bach.project.resolveTarget().resolve("test"), 1).forEach(classPath::add);
            Files.walk(bach.project.resolveTargetMods(), 1).forEach(classPath::add);
            Files.walk(bach.project.resolveAuxResolved()).forEach(classPath::add);
        } catch (IOException e) {
            throw new Error("Generating class-path failed", e);
        }
        return classPath;
    }

    private void resolve(Path ideaLibraryDirectory) throws IOException {
        Path mavenRepository = Paths.get(System.getProperty("user.home"), ".m2", "repository");
        System.out.println("Resolving dependencies using local maven repository: " +
                                   mavenRepository);
        Files.walk(ideaLibraryDirectory)
                .filter(path -> path.getFileName().toString().endsWith(".xml"))
                .forEach(xml -> resolve(mavenRepository, xml));
    }

    private void resolve(Path mavenRepository, Path ideaLibraryFile) {
        System.out.println("Resolving " + ideaLibraryFile);
        Path resolved = Paths.get(".bach", "resolved");
        try {
            Files.createDirectories(resolved);
            List<String> lines = Files.readAllLines(ideaLibraryFile);
            boolean parse = false;
            for (String line : lines) {
                line = line.trim();
                if (!parse) {
                    if (line.equals("<CLASSES>")) {
                        parse = true;
                    }
                    continue;
                }
                if (line.equals("</CLASSES>")) {
                    return;
                }
                line = line.substring("<root url=\"jar://$MAVEN_REPOSITORY$/".length());
                line = line.substring(0, line.indexOf("!/\" />"));
                Path source = mavenRepository.resolve(line);
                Path target = resolved.resolve(source.getFileName());
                if (Files.exists(source)) {
                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    continue;
                }
                // extract maven artifact coordinates
                String base = line.substring(0, line.lastIndexOf('/'));
                System.out.println(base);
                String version = base.substring(base.lastIndexOf('/') + 1, base.length());
                System.out.println(version);
                String artifact =
                        line.substring(line.lastIndexOf('/') + 1, line.lastIndexOf("-" + version));
                System.out.println(artifact);
                String group = line.substring(0, line.lastIndexOf("/" + artifact + "/"));
                System.out.println(group);
                bach.resolve(group, artifact, version);
            }
        } catch (IOException e) {
            throw new Error("resolving " + ideaLibraryFile + " failed", e);
        }
    }

    private void test(String classPath) throws IOException {
        String repo = "http://repo1.maven.org/maven2";
        String user = "org/junit/platform";
        String name = "junit-platform-console-standalone";
        String version = "1.0.0-M4";
        String file = name + "-" + version + ".jar";
        URI uri = URI.create(String.join("/", repo, user, name, version, file));
        Path path = Paths.get(".bach/tools").resolve(name);
        Path jar = bach.download(uri, path, file, p -> true);
        bach.call("java", "-ea", "-jar", jar, "--class-path", classPath, "--scan-classpath");
    }
}
