import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
}

group = "me.aroze"
version = "4.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(libs.kotlinx)
    implementation(libs.mongodb)
    implementation(libs.jda.fork) // Temporary fork for components v2 support
    implementation(libs.ktoml)
    implementation(libs.auto.service)
    implementation(libs.autoservice.google)
    implementation(libs.bundles.cloud)
    implementation(libs.bundles.log4j)
    ksp(libs.autoservice.ksp)
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.withType<KotlinCompile>() {
    compilerOptions {
        freeCompilerArgs.add("-java-parameters")
    }
}
