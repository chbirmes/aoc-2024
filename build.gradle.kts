plugins {
    kotlin("jvm") version "2.0.20"
    id("org.jetbrains.compose") version "1.7.1"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
}