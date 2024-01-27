plugins {
    id("application")
    id("org.gradlex.extra-java-module-info").version("1.4.2")
}

dependencies {
    implementation(project(":com.mechanitis.demo.sense.flow"))
    implementation(project(":com.mechanitis.demo.sense.service"))

    testImplementation(project(":com.mechanitis.demo.sense.service.test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("com.mechanitis.demo.sense.user.UserService")
}

extraJavaModuleInfo {
    // this I seem to need
    automaticModule("jakarta.websocket-api-1.1.jar", "jakarta.websocket.api")
    // these I don"t need explicitly, but the build fails without them
    automaticModule("jakarta.websocket-client-api-1.0.jar", "org.eclipse.jetty.websocket.client")
    automaticModule("javax.servlet-api-3.1.0.jar", "javax.servlet.api")
}