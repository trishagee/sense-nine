plugins {
    id("application")
}

dependencies {
    implementation(project(":com.mechanitis.demo.sense.flow"))
    implementation(project(":com.mechanitis.demo.sense.service"))

    testImplementation(project(":com.mechanitis.demo.sense.service.test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.14.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.14.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.14.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.mechanitis.demo.sense.user.UserService")
}
