plugins {
    id( "java-library")
    jacoco
}

dependencies {
    api( "org.reactivestreams:reactive-streams:1.0.4")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.14.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.14.0")
    testImplementation("org.mockito:mockito-core:5.20.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.14.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.14.0")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}