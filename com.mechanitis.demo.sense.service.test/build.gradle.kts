plugins {
    id("java-library")
}

dependencies {
    implementation(project(":com.mechanitis.demo.sense.service"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
