package me.aroze.snuggles.config

import com.google.gson.Gson
import org.apache.commons.io.FileUtils
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

object ConfigLoader {

    lateinit var config: BotConfig

    fun load() {
        val config = this::class.java.classLoader.getResourceAsStream("config.json") ?: return
        val cwd = Paths.get("").toAbsolutePath()
        val configFile = cwd.resolve("./config.json").toFile()

        if (!configFile.exists()) {
            configFile.createNewFile()
            FileUtils.copyInputStreamToFile(config, configFile)
        }

        this.config = Gson().fromJson(FileUtils.readFileToString(configFile, StandardCharsets.UTF_8), BotConfig::class.java)
    }

}