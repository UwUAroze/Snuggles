package me.aroze.snuggles.listeners.impl

import instance
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.models.ChannelData
import me.aroze.snuggles.models.LoggedMessage
import me.aroze.snuggles.utils.BarStyle
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.utils.FileUpload
import java.io.InputStream
import java.net.URL

object LoggingHandler {

    fun handleMessageRecieve(event: MessageReceivedEvent) = runBlocking {

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

    fun handleMessageUpdate(event: MessageUpdateEvent, loggedMessage: LoggedMessage) = runBlocking {

        if (!event.isFromGuild) return@runBlocking
        if (event.message.author.isBot || event.message.author.isSystem) return@runBlocking

        launch {

            val channelData = ChannelData.getByGuild(event.guild.id).firstOrNull { it.logging != null }
            val logData = channelData?.logging ?: return@launch
            if (logData.disabled || !logData.logMessageChanges) return@launch

            val loggingChannel = event.guild.getTextChannelById(channelData.channel) ?: return@launch

            val eb = FancyEmbed()
                .setAuthor(
                    "A message by ${event.author.asTag} was edited in #${event.channel.name}",
                    null,
                    event.author.effectiveAvatarUrl
                )
                .setDescription(
                    "\n > Message before edit: " +
                        (if (loggedMessage.content.isEmpty()) "`(None; message had no text content)`" else "\n" + loggedMessage.content) + "\n" +
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

    fun handleMessageDelete(guild: Guild, channel: TextChannel, message: LoggedMessage) {

        val channelData = ChannelData.getByGuild(guild.id).firstOrNull { it.logging != null }
        val logData = channelData?.logging ?: return
        if (logData.disabled || !logData.logMessageChanges) return

        val author = instance.retrieveUserById(message.author).complete()

        val loggingChannel = guild.getTextChannelById(channelData.channel) ?: return

        val eb = FancyEmbed()
            .setAuthor(
                "A message by ${author.asTag} was deleted in #${channel.name}",
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
                " > Channel: ${channel.asMention} `(${channel.id})`\n" +
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