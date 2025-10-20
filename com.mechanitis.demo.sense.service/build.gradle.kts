plugins {
    id("java-library")
}

dependencies {
    api("org.eclipse.jetty.websocket:websocket-jakarta-server:11.0.26")
    api("jakarta.websocket:jakarta.websocket-client-api:2.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.14.0")
    testImplementation("org.mockito:mockito-core:5.20.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.14.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.14.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}
repositories {
    mavenCentral()
}
