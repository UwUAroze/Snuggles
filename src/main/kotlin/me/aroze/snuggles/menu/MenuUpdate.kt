package me.aroze.snuggles.menu

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

data class MenuUpdate(
    val channel: TextChannel?,
    val settings: List<String>
)
