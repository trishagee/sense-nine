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
