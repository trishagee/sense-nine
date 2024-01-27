plugins {
    id("java-library")
    id("org.gradlex.extra-java-module-info").version("1.4.2")
}

dependencies {
    implementation("org.eclipse.jetty.websocket:javax-websocket-server-impl:9.4.52.v20230823")
    api("javax.websocket:javax.websocket-api:1.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    testImplementation("org.mockito:mockito-core:5.5.0")
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
    automaticModule("javax.websocket-api-1.1.jar", "javax.websocket.api")
    // these I don"t need explicitly, but the build fails without them
    automaticModule("javax.websocket-client-api-1.0.jar", "org.eclipse.jetty.websocket.client")
    automaticModule("javax.servlet-api-3.1.0.jar", "javax.servlet.api")
}