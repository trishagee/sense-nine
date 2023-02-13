plugins {
    id("java-library")
    id("org.gradlex.extra-java-module-info").version("1.3")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.jetty.websocket:javax-websocket-server-impl:9.4.50.v20221201")
    api("javax.websocket:javax.websocket-api:1.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    testImplementation("org.mockito:mockito-core:5.1.1")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

java {
    modularity.inferModulePath.set(true)
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