package me.aroze.snuggles.commands.handler

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

data class CommandEvent(
    val message: SlashCommandEvent,
    var silent: Boolean = false,
)
