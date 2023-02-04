plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "me.aroze.snuggles"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:c525f6f")
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