package me.aroze.snuggles.config

import com.moandjiezana.toml.Toml
import org.apache.commons.io.FileUtils
import java.nio.file.Paths

object ConfigLoader {

    lateinit var config: Toml

    fun load() {
        val config = this::class.java.classLoader.getResourceAsStream("config.toml") ?: return
        val cwd = Paths.get("").toAbsolutePath()
        val configFile = cwd.resolve("config.toml").toFile()

        if (!configFile.exists()) {
            configFile.createNewFile()
            FileUtils.copyInputStreamToFile(config, configFile)
        }

        ConfigLoader.config = Toml().read(configFile)
    }

}