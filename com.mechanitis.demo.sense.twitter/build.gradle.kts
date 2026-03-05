plugins {
    id("application")
    jacoco
}

dependencies {
    implementation(project(":com.mechanitis.demo.sense.flow"))
    implementation(project(":com.mechanitis.demo.sense.service"))

    implementation("io.reactivex.rxjava2:rxjava:2.2.21")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.14.0")
    testImplementation("org.hamcrest:hamcrest:2.2")

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
repositories {
    mavenCentral()
}

application {
    mainClass.set("com.mechanitis.demo.sense.twitter.CannedTweetsService")
}

