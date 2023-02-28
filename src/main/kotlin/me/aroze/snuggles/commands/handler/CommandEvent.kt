package me.aroze.snuggles.commands.handler

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

data class CommandEvent(
    val message: SlashCommandInteractionEvent,
    var silent: Boolean = false,
)
