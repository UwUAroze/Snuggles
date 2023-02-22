package me.aroze.snuggles.commands

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData

abstract class BaseCommand(private val name: String, private val description: String) : ListenerAdapter() {
    protected val options = ArrayList<Option>()
    protected val guildOnly = false
    protected var silent = false
    open val defaultSilent = false

    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.name == name) {
            if (guildOnly && event.guild == null) {
                // TODO: Send error message to user
                return
            }
            silent = event.getOption("silent")?.asBoolean ?: defaultSilent
            onExecute(event)
        }
    }

    val build: CommandData
        get() {
            val command = CommandData(name, description)
            for (option in options) {
                command.addOption(
                    option.type,
                    option.name,
                    option.description,
                    option.required
                )
            }
            command.addOption(
                OptionType.BOOLEAN,
                "silent",
                "Whether command output should only be visible to you",
            )
            return command
        }

    abstract fun onExecute(event: SlashCommandEvent)
    class Option(var type: OptionType, var name: String, var description: String, var required: Boolean)

}