plugins {
    id("com.gradle.enterprise") version "3.14.1"
    id("com.gradle.common-custom-user-data-gradle-plugin") version "1.11.1"
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

gradleEnterprise {
    server = "https://18.212.219.143.nip.io"
    allowUntrustedServer = true
    buildScan {
        publishAlways()
    }
}