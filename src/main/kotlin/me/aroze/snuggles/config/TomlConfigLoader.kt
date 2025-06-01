package me.aroze.snuggles.config

import com.akuleshov7.ktoml.Toml
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.serializer
import java.io.File
import java.nio.file.Files
import kotlin.jvm.java

/**
 * A utility class for loading, defaulting and parsing TOML configuration files.
 *
 * @param configPath The path to the configuration file.
 * @param configClass The class type of the configuration to be loaded.
 * @param T The type of the configuration object.
 */
class TomlConfigLoader<T>(
    private val configPath: String,
    private val configClass: Class<T>
) {

    /**
     * Loads the configuration from the specified path, creating a default configuration file if it does not exist.
     *
     * @throws IllegalStateException if there's no config file, and no default configuration file is found in the jar.
     * @throws SerializationException if the configuration file cannot be parsed.
     */
    fun load(): T {
        val configFile = File(configPath)

        if (!configFile.exists()) {
            // Create default configuration
            val stream = TomlConfigLoader::class.java.getResourceAsStream("/${configPath}")
                ?: error("Could not find default configuration file in jar resources. Path: $configPath")

            if (!configFile.exists()) Files.copy(stream, configFile.toPath())
        }

        @Suppress("UNCHECKED_CAST")
        val serializer = serializer(configClass) as KSerializer<T>

        return Toml.decodeFromString(serializer, configFile.readText())
    }

}

inline fun <reified T> TomlConfigLoader(
    configPath: String
): TomlConfigLoader<T> {
    return TomlConfigLoader(configPath, T::class.java)
}
