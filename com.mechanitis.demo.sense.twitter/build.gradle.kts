plugins {
    id("application")
    id("org.gradlex.extra-java-module-info").version("1.3")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":com.mechanitis.demo.sense.flow"))
    implementation(project(":com.mechanitis.demo.sense.service"))
    implementation(project(":com.mechanitis.demo.sense.service"))

    implementation("io.reactivex.rxjava2:rxjava:2.2.21")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    testImplementation("org.hamcrest:hamcrest:2.2")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

java {
    modularity.inferModulePath.set(true)
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("com.mechanitis.demo.sense.twitter.CannedTweetsService")
}

extraJavaModuleInfo {
    automaticModule("javax.websocket-api-1.1.jar", "javax.websocket.api")
    automaticModule("javax.websocket-client-api-1.0.jar", "org.eclipse.jetty.websocket.client")
    automaticModule("javax.servlet-api-3.1.0.jar", "javax.servlet.api")
}
