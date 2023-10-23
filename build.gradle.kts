plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    application
}

group = "me.aroze"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.15")
    implementation("com.github.SantioMC.Coffee:jda:23a8cdcb69")

    implementation("com.github.uwuaroze:arozeutils:3b3a312dc4")

    implementation("com.akuleshov7:ktoml-core-jvm:0.5.0")
    implementation("org.mongodb:mongodb-driver-kotlin-sync:4.11.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("me.aroze.snuggles.SnugglesKt")
}