plugins {
    id("application")
}

dependencies {
    implementation(project(":com.mechanitis.demo.sense.flow"))
    implementation(project(":com.mechanitis.demo.sense.service"))

    testImplementation(project(":com.mechanitis.demo.sense.service.test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
    }
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.mechanitis.demo.sense.user.UserService")
}
