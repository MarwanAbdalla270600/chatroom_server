plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2" // Add the Shadow plugin
}

group = "fhtw"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // JUnit
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // other dependencies...
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.0-rc1")

    compileOnly ("org.projectlombok:lombok:1.18.30")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")

    testCompileOnly ("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.30")
}

tasks.test {
    useJUnitPlatform()
}
tasks.jar {
    manifest {
        attributes("Main-Class" to "fhtw.Main")
    }
}