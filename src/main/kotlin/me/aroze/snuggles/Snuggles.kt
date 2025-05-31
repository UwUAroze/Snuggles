package me.aroze.snuggles

import me.aroze.snuggles.config.Config
import me.aroze.snuggles.config.TomlConfigLoader
import me.aroze.snuggles.initialisation.BotLoader
import net.dv8tion.jda.api.JDA

lateinit var snuggles: JDA
    private set

lateinit var config: Config
    private set

fun main() {

    config = TomlConfigLoader<Config>("config.toml")
        .load()

    snuggles = BotLoader(config.authentication.token)
        .loadDefault()

}
