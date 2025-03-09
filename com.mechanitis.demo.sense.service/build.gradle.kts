plugins {
    id("java-library")
}

dependencies {
    api("org.eclipse.jetty.websocket:websocket-jakarta-server:12.0.12")
    api("jakarta.websocket:jakarta.websocket-client-api:2.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation("org.mockito:mockito-core:5.12.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
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
