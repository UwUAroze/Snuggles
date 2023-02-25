package me.aroze.snuggles.initialisation

import instance
import me.aroze.snuggles.config.ConfigLoader
import net.dv8tion.jda.api.JDABuilder

object Login {

    fun login() {
        instance = JDABuilder.createDefault(ConfigLoader.config.getString("authentication.token")).build()
    }

}