package me.aroze.snuggles.listeners

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.database.database
import me.aroze.snuggles.models.ChannelData
import me.aroze.snuggles.models.LoggedMessage
import me.aroze.snuggles.utils.BarStyle
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.utils.FileUpload
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import java.io.InputStream
import java.net.URL

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

            val channelData = ChannelData.get(event.channel.id)
            val logData = channelData?.logging ?: return@launch
            if (logData.disabled || !logData.logMessageChanges) return@launch

            val collection = database.getCollection<LoggedMessage>()
            val previousMessage = collection.findOne(LoggedMessage::message eq event.messageId) ?: return@launch

            if (previousMessage.content == event.message.contentRaw) return@launch
            previousMessage.edit(event.message.contentRaw)

            val loggingChannel = event.guild.getTextChannelById(channelData.channel) ?: return@launch

            val eb = FancyEmbed()
                .setAuthor(
                    "A message by ${event.author.asTag} was edited in #${event.channel.name}",
                    null,
                    event.author.effectiveAvatarUrl
                )
                .setDescription(
                    "\n > Message before edit: " +
                        (if (previousMessage.content.isEmpty()) "`(None; message had no text content)`" else "\n" + previousMessage.content) + "\n" +
                        "\n > Message after edit:\n" +
                        event.message.contentRaw + "\n" +
                        "\n" +
                        " > Perpetrator: ${event.author.asMention} `(${event.author.id})`\n" +
                        " > Channel: ${event.channel.asMention} `(${event.channel.id})`\n" +
                        " > Click here to [jump to message](https://discord.com/channels/${event.message.guild.id}/${event.channel.id}/${event.messageId})"
                )
                .build()

            loggingChannel.sendMessageEmbeds(eb)
                .bar(BarStyle.PINK)
                .queue()

        }

    }

    override fun onMessageDelete(event: MessageDeleteEvent) = runBlocking {

        if (!event.isFromGuild) return@runBlocking

        launch {
            val channelData = ChannelData.get(event.channel.id)
            val logData = channelData?.logging ?: return@launch
            if (logData.disabled || !logData.logMessageChanges) return@launch

            val collection = database.getCollection<LoggedMessage>()
            val message = collection.findOne(LoggedMessage::message eq event.messageId) ?: return@launch
            val author = event.jda.retrieveUserById(message.author).complete()

            if (author.isBot || author.isSystem) return@launch

            message.delete()

            val loggingChannel = event.guild.getTextChannelById(channelData.channel) ?: return@launch

            val eb = FancyEmbed()
                .setAuthor(
                    "A message by ${author.asTag} was deleted in #${event.channel.name}",
                    null,
                    author.effectiveAvatarUrl
                )

            val streams = mutableListOf<InputStream>()
            val fileUploads = mutableListOf<FileUpload>()

            var description =
                "\n > Deleted content: " +
                (if (message.content.isEmpty()) "`(None; message had no text content)`" else "\n" + message.content + "\n") +
                "\n" +
                " > Perpetrator: ${author.asMention} `(${author.id})`\n" +
                " > Channel: ${event.channel.asMention} `(${event.channel.id})`\n" +
                "\n"

            if (message.attachments.isNotEmpty()) {
                description += "\n > Attachments: ${message.attachments.size} (uploaded with this message) \n"
                message.attachments.forEach {
                    val stream = URL(it.url).openStream()
                    streams.add(stream)
                    fileUploads.add(FileUpload.fromData(stream, it.name))
                }
            }


            eb.setDescription(description)
                .build()

            loggingChannel.sendMessageEmbeds(eb.build())
                .bar(BarStyle.ERROR)
                .addFiles(fileUploads)
                .queue {
                    streams.forEach { it.close() }
                }
        }
    }
}