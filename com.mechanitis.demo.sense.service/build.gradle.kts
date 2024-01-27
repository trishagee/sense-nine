plugins {
    id("java-library")
}

dependencies {
    api("org.eclipse.jetty.websocket:websocket-jakarta-server:11.0.19")
    api("jakarta.websocket:jakarta.websocket-client-api:2.1.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")

    testImplementation("org.mockito:mockito-core:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
