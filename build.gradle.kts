group = "it.intre"
version = "2.0-SNAPSHOT"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("org.openjfx.javafxplugin") version "0.1.0"

    application
}

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.1")

    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.slf4j:slf4j-api:1.7.5")
    implementation("org.slf4j:slf4j-reload4j:2.0.7")
}

javafx {
    version = "21.0.2"
    modules("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("it.intre.conf.raffle.RaffleAppKt")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
