//$JAVA_HOME/bin/jshell --show-version $0 $@; exit $?

URL url = new URL("https://raw.githubusercontent.com/sormuras/bach/master/src/main/java/Bach.java");
Path target = Paths.get(".bach")
Path script = target.resolve("Bach.java")
if (Files.notExists(script)) {
  Files.createDirectories(target);
  try (InputStream stream = url.openStream()) { Files.copy(stream, script); }
}

/open .bach/Bach.java
/open .bach/BuildSenseNine.java

BuildSenseNine.main()

/exit
