plugins {
    id("application")
    id("org.gradlex.extra-java-module-info").version("1.4.2")
    id("org.openjfx.javafxplugin").version("0.1.0")
}

javafx {
    version = "17.0.8"
    modules("javafx.controls", "javafx.graphics", "javafx.fxml")
}

dependencies {
    implementation(project(":com.mechanitis.demo.sense.service"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

extraJavaModuleInfo {
    // this I need
    automaticModule("jakarta.websocket-api-1.1.jar", "jakarta.websocket.api")
    // these I don"t need explicitly, but the build fails without them
    automaticModule("jakarta.websocket-client-api-1.0.jar", "org.eclipse.jetty.websocket.client")
    automaticModule("javax.servlet-api-3.1.0.jar", "javax.servlet.api")
}

application {
    mainModule.set("com.mechanitis.demo.sense.client")
    mainClass.set("com.mechanitis.demo.sense.client.Dashboard")
}
