plugins {
    id("application")
}

dependencies {
    implementation(project(":com.mechanitis.demo.sense.flow"))
    implementation(project(":com.mechanitis.demo.sense.service"))

    implementation("io.reactivex.rxjava2:rxjava:2.2.21")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation("org.hamcrest:hamcrest:2.2")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}
repositories {
    mavenCentral()
}

application {
    mainClass.set("com.mechanitis.demo.sense.twitter.CannedTweetsService")
}
