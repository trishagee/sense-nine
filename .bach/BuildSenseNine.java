import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class BuildSenseNine {

  public static void main(String... args) throws IOException {
    Bach bach = new Bach();

    // resolve dependencies (for "main" and "test")
    resolve(bach, Paths.get(".idea", "libraries"));

    // compile main
    bach.javac(
        javacOptions -> {
          javacOptions.modulePaths = List.of(Paths.get(".bach", "resolved"));
          javacOptions.moduleSourcePaths = List.of(Paths.get("src"));
          javacOptions.destinationPath = Paths.get(".bach/out/javac/main");
          return javacOptions;
        });

    // compile tests (error: not in a module on the module source path)
    //    bach.javac(
    //        javacOptions -> {
    //          javacOptions.modulePaths = List.of(Paths.get(".bach", "resolved"));
    //          javacOptions.moduleSourcePaths = List.of(Paths.get("test"));
    //          javacOptions.destinationPath = Paths.get(".bach/out/javac/test");
    //          return javacOptions;
    //        });

    // compile test (using legacy class-path)
    List<Path> jars = Files.walk(Paths.get(".bach", "resolved")).collect(Collectors.toList());
    List<Path> mods = Files.walk(Paths.get(".bach/out/javac/main"), 1).collect(Collectors.toList());
    List<Path> classPath = new ArrayList<>();
    classPath.addAll(mods);
    classPath.addAll(jars);
    bach.javac(
        javacOptions -> {
          javacOptions.classPaths = classPath;
          javacOptions.classSourcePaths = List.of(Paths.get("test"));
          javacOptions.destinationPath = Paths.get(".bach/out/javac/test");
          return javacOptions;
        });

    // run tests (on the class-path...)
    classPath.add(Paths.get(".bach/out/javac/test"));
    test(bach, classPath.stream().map(Object::toString).collect(joining(File.pathSeparator)));
  }

  private static void resolve(Bach bach, Path ideaLibraryDirectory) throws IOException {
    Path mavenRepository = Paths.get(System.getProperty("user.home"), ".m2", "repository");
    System.out.println("using local maven repository: " + mavenRepository);
    Files.walk(ideaLibraryDirectory)
        .filter(path -> path.getFileName().toString().endsWith(".xml"))
        .forEach(xml -> resolve(bach, mavenRepository, xml));
  }

  private static void resolve(Bach bach, Path mavenRepository, Path ideaLibraryFile) {
    System.out.println("resolving " + ideaLibraryFile);
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

  private static void test(Bach bach, String classPath) throws IOException {
    String repo = "http://repo1.maven.org/maven2";
    String user = "org/junit/platform";
    String name = "junit-platform-console-standalone";
    String version = "1.0.0-M5";
    String file = name + "-" + version + ".jar";
    URI uri = URI.create(String.join("/", repo, user, name, version, file));
    Path path = Paths.get(".bach/tools").resolve(name);
    Path jar = bach.download(uri, path, file, p -> true);
    bach.call("java", "-ea", "-jar", jar, "--class-path", classPath, "--scan-classpath");
  }
}
