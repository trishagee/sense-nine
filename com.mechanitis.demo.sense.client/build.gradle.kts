plugins {
    id("application")
    id("org.openjfx.javafxplugin").version("0.1.0")
}

javafx {
    version = "22.0.2"
    modules("javafx.controls", "javafx.graphics", "javafx.fxml")
}

dependencies {
    implementation(project(":com.mechanitis.demo.sense.service"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.3")
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

application {
    mainModule.set("com.mechanitis.demo.sense.client")
    mainClass.set("com.mechanitis.demo.sense.client.Dashboard")
}
