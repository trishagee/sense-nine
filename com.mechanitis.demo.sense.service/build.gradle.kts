plugins {
    id("java-library")
}

dependencies {
    api("org.eclipse.jetty.websocket:websocket-jakarta-server:11.0.21")
    api("jakarta.websocket:jakarta.websocket-client-api:2.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

    testImplementation("org.mockito:mockito-core:5.12.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
repositories {
    mavenCentral()
}
