package me.aroze.snuggles.config

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.decodeFromString
import java.io.File
import java.nio.file.Files

object ConfigLoader {

    lateinit var config: Config

    fun load() {
        val configFile = File("config.toml")
        val stream = ConfigLoader::class.java.getResourceAsStream("/config.toml")
            ?: return println("Could not find config.toml in resources")
        if (!configFile.exists()) Files.copy(stream, configFile.toPath())

        config = Toml.decodeFromString<Config>(configFile.readText())
    }

}