plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "me.aroze.snuggles"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.DV8FromTheWorld:JDA:v4.4.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("me.aroze.snuggles.MainKt")
}