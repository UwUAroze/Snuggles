package me.aroze.snuggles.listeners

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.listeners.impl.AutocompleteHandler
import me.aroze.snuggles.listeners.impl.CountingHandler
import me.aroze.snuggles.listeners.impl.LoggingHandler
import me.aroze.snuggles.models.ChannelData
import me.aroze.snuggles.models.LoggedMessage
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
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

    override fun onMessageUpdate(event: MessageUpdateEvent): Unit = runBlocking {
        launch {
            val channel = event.channel as? TextChannel ?: return@launch
            val channelData = ChannelData.getByChannel(event.channel.id) ?: return@launch
            val loggedMessage = LoggedMessage.getByMessageId(event.messageId) ?: return@launch

            if (loggedMessage.content != event.message.contentRaw) loggedMessage.edit(event.message.contentRaw)

            LoggingHandler.handleMessageUpdate(event, loggedMessage)
            CountingHandler.handleMessageUpdate(channelData, channel, loggedMessage)
        }
    }

    override fun onMessageDelete(event: MessageDeleteEvent): Unit = runBlocking {
        launch {
            val channel = event.channel as? TextChannel ?: return@launch
            val channelData = ChannelData.getByChannel(event.channel.id) ?: return@launch
            val message = LoggedMessage.getByMessageId(event.messageId) ?: return@launch

            LoggingHandler.handleMessageDelete(event.guild, channel, message)
            CountingHandler.handleMessageDelete(channelData, channel, message)

            message.delete()
        }
    }

    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        AutocompleteHandler.handleAutoComplete(event)
    }



}