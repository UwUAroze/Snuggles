package me.aroze.snuggles.commands

import me.aroze.snuggles.database.Database
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.Command.Subcommand
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.OptionData

abstract class BaseCommand(private val name: String, private val description: String) : ListenerAdapter() {
    protected val options = ArrayList<Option>()
    protected var silent = false
    open val guildOnly = true
    open val devOnly = false
    open val defaultSilent = false

    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.name == name.split(" ")[0]) {
            if (guildOnly && event.guild == null) {
                // TODO: Send error message to user
                return
            }

            Database.botStats.totalExecutions++
            silent = event.getOption("silent")?.asBoolean ?: defaultSilent
            onExecute(event)
        }
    }

    val optionData: List<OptionData>
        get() {
            return options.map {
                OptionData(it.type, it.name, it.description, it.required)
            }
        }

    val build: CommandData
        get() {
            val command = CommandData(name.split(" ")[0], description)
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