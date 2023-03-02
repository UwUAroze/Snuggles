package me.aroze.snuggles.listeners

import me.aroze.snuggles.commands.handler.Autocomplete
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandHandler
import me.aroze.snuggles.utils.kotlinParameter
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.lang.reflect.Method

object AutocompleteListener : ListenerAdapter() {

    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {

        val command = CommandHandler.commands.find { it.build.name == event.name } ?: return

        val execution: Method = if (event.subcommandName != null) {
            Command.getSubCommands(command.clazz).find { it.name == event.subcommandName } ?: return
        } else Command.getMainCommand(command.clazz) ?: return

        val field = execution.parameters.find { it.kotlinParameter(execution).name == event.focusedOption.name } ?: return
        val autocomplete = field.getAnnotation(Autocomplete::class.java) ?: return

        event.replyChoiceStrings(autocomplete.options.toList()).queue()

    }

}
