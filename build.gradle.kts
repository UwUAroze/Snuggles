import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
    java
}

group = "me.aroze.snuggles"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.DV8FromTheWorld:JDA:v4.4.0")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.UwUAroze:ArozeUtils:205fe4677d")
    implementation("org.litote.kmongo:kmongo:4.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.reflections:reflections:0.10.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("Snuggles")
}

tasks {
    jar {
        manifest {
            attributes("Main-Class" to "Snuggles")
        }
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        archiveFileName.set("Snuggles.jar")
    }
}