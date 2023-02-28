package me.aroze.snuggles.commands.handler

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

data class CommandBundle(
    val annotation: Command,
    val clazz: Class<*>,
    val build: SlashCommandData,
    val instance: Any,
    val subBuilds: MutableList<SubcommandData> = mutableListOf()
)
