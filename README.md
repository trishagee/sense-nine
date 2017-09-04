# sense-nine

Repository containing the demo code for Real World Java 9.  

For more information about the talk (including slides, videos, and more detailed research) see [Real World Java 9](https://trishagee.github.io/presentation/real_world_java_9/).

### Branches
This has a slightly complicated set of branches, as it's a) a playground for a bunch of Java 9 features and b) has evolved as Java 9 has evolved.

**master** the original version of the app working with Java 9 and Reactive Streams. It's quite old now and may no longer compile with the latest Java 9.

**broken-j9** this branch intentionally does not compile under Java 9, and shows the type of compilation errors you might expect to see when you first start using it.

**jigsaw** this is intentionally missing a module-info.java for com.mechanitis.demo.sense.flow. If you check out this branch you can play with creating one and fixing up errors.

**start-point** this is the state of the application before the demo.  The latest version now has a Maven/Gradle-like directory structure, but you can see earlier versions follow the OpenJDK structure.

**jbcnconf** shows the state of the completed application. This is the latest version of the demo, was designed to work with IntelliJ IDEA 2017.2 and Java 9 b176.

**qcon-london** the completed application as demoed at QCon London in March 2017.  This version of the presentation focused on migrating an application to use the Reactive Streams API from a custom messaging solution.  This worked with a much earlier version of Java 9 and may no longer compile.



[![JetBrains team project](http://jb.gg/badges/team.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
