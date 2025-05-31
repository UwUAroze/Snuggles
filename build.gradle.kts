plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
}

group = "me.aroze"
version = "4.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")
    implementation("net.dv8tion:JDA:5.5.1")
    implementation("com.akuleshov7:ktoml-core:0.7.0")
}

kotlin {
    jvmToolchain(21)
}
