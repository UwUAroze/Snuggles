package me.aroze.snuggles.listeners

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object EventListener : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        LoggingHandler.handleMessageRecieve(event)
        CountingHandler.handleMessageRecieve(event)
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        LoggingHandler.handleMessageUpdate(event)
    }

    override fun onMessageDelete(event: MessageDeleteEvent) {
        LoggingHandler.handleMessageDelete(event)
    }

    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        AutocompleteListener.onCommandAutoCompleteInteraction(event)
    }



}