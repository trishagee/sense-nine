plugins {
    id("com.gradle.develocity") version("3.17.5")
    id("com.gradle.common-custom-user-data-gradle-plugin") version "2.0.2"
}

develocity {
    server.set("https://dpeuniversity-develocity.gradle.com/")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "sense-nine"

include("com.mechanitis.demo.sense.flow",
        "com.mechanitis.demo.sense.client",
        "com.mechanitis.demo.sense.mood",
        "com.mechanitis.demo.sense.service",
        "com.mechanitis.demo.sense.service.test",
        "com.mechanitis.demo.sense.twitter",
        "com.mechanitis.demo.sense.user")
