package me.aroze.snuggles.initialisation

import jda
import me.aroze.snuggles.config.ConfigLoader
import net.dv8tion.jda.api.JDABuilder

object Login {

    fun login() {
        jda = JDABuilder.createDefault(ConfigLoader.config.token).build()
    }

}