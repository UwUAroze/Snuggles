package me.aroze.snuggles.listeners

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.database.database
import me.aroze.snuggles.models.LoggedMessage
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

object LoggingListener: ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) = runBlocking {

        if (!event.isFromGuild) return@runBlocking
        if (event.message.author.isBot || event.isWebhookMessage || event.message.author.isSystem) return@runBlocking

        val attachments = event.message.attachments.map { it.url }

        launch {
            LoggedMessage(
                System.currentTimeMillis(),
                event.messageId,
                event.guild.id,
                event.author.id,
                event.message.contentRaw,
                attachments.map { LoggedMessage.AttachmentInfo(it, it.substringAfterLast("/")) }.toMutableList()
            ).save()
        }

    }

    override fun onMessageUpdate(event: MessageUpdateEvent) = runBlocking {

        if (!event.isFromGuild) return@runBlocking
        if (event.message.author.isBot || event.message.author.isSystem) return@runBlocking

        launch {
            val collection = database.getCollection<LoggedMessage>()
            val logged = collection.findOne(LoggedMessage::message eq event.messageId) ?: return@launch
            if (logged.content == event.message.contentRaw) return@launch
            logged.edit(event.message.contentRaw)
        }
    }

    override fun onMessageDelete(event: MessageDeleteEvent) {
        //
    }

}