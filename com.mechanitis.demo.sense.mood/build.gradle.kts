plugins {
    id("application")
}

dependencies {
    implementation(project(":com.mechanitis.demo.sense.flow"))
    implementation(project(":com.mechanitis.demo.sense.service"))
    implementation("io.projectreactor:reactor-core:3.6.7")

    testImplementation(project(":com.mechanitis.demo.sense.service.test"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.4")
    testImplementation("org.mockito:mockito-core:5.12.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
    }
}
repositories {
    mavenCentral()
}

application {
    mainClass.set("com.mechanitis.demo.sense.mood.MoodService")
}

