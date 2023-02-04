package me.aroze.snuggles.initialisation

import jda
import net.dv8tion.jda.api.JDABuilder

object Login {

    fun login() {
        jda = JDABuilder.createDefault("MTA3MTUwMDg5OTE4NjM4OTEzMg.Guf81p.Lx5evZrMA8ewYLF-IBSKzqsx5e0LHoAA0WYbjQ").build()
    }

}