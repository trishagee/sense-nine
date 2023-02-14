plugins {
    id ("java-library")
    // needed to create a "module" out of non-modular libraries
    id ("org.gradlex.extra-java-module-info") version "1.3"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation (project(":com.mechanitis.demo.sense.service"))
}

java {
    modularity.inferModulePath.set(true)
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

extraJavaModuleInfo {
    // this I seem to need
    automaticModule("javax.websocket-api-1.1.jar", "javax.websocket.api")
}