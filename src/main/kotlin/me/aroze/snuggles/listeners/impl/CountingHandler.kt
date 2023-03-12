package me.aroze.snuggles.listeners.impl

import com.github.keelar.exprk.Expressions
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.models.ChannelData
import me.aroze.snuggles.models.LoggedMessage
import me.aroze.snuggles.models.UserData
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object CountingHandler {

    private val tracker = mutableMapOf<String, Int>()

    fun handleMessageReceive(event: MessageReceivedEvent) = runBlocking {
        if (!event.isFromGuild) return@runBlocking

        launch {
            val count = ChannelData.getByChannel(event.channel.id)?.counting ?: return@launch

            if (event.isWebhookMessage || event.message.author.isBot) {
                if (event.author.id != event.jda.selfUser.id) return@launch
                count.lastBotMessage = event.messageId
                return@launch
            }

            if (tracker[event.guild.id] != null && tracker[event.guild.id]!! == count.count && count.count != 0) {
                count.count = tracker[event.guild.id]!!
            }

            val number = try { Expressions().eval(event.message.contentDisplay) }
            catch (e: Exception) {
                if (!count.allowTalking) event.message.delete().queue()
                return@launch
            }

            val beNice = count.kinderMessages

            if (number != (count.count + 1).toBigDecimal()) {
                event.message.addReaction(Emoji.fromFormatted("<a:bonk:1081320319500955718>")).queue()
                event.message.channel.sendMessage(
                    if (beNice) "${event.author.asMention} ruined it at **${count.count}**, I authorize you all to yell at them, but be nice!"
                    else "Everyone look, this moron ${event.author.asMention} FUCKED IT UP at **${count.count}**. Go back to school you dumbass."
                ).queue()
                count.resetCurrentCount()
                return@launch
            }

            if (!count.allowConsecutiveUsers && count.lastCounter == event.author.id) {
                event.message.addReaction(Emoji.fromFormatted("<a:bonk:1081320319500955718>")).queue()
                event.message.channel.sendMessage(
                    if (beNice) "You ruined it ${event.author.asMention}! You aren't allowed to type two numbers in a row."
                    else "Jesus Christ ${event.author.asMention}, you're such a failure, you can't type two numbers in a row."
                ).queue()
                count.resetCurrentCount()
                return@launch
            }

            val userData = UserData.get(event.author.id, event.guild.id) ?: UserData.create(event.author.id, event.guild.id)

            Database.botStats.totalCounts++
            userData.totalCounts++
            count.count++
            tracker[event.guild.id] = count.count
            count.lastCounter = event.author.id
            val highScore = count.count > count.highScore

            if (highScore) count.highScore = count.count

            var reaction: Emoji = Emoji.fromFormatted("<:tick_pink:1081999667287568384>")
            if (highScore) reaction = Emoji.fromFormatted("<:tick_viola:1082002568005296279>")
            if (count.count == 100) reaction = Emoji.fromUnicode("\uD83D\uDCAF")
            if (count.count.toString().contains("69")) reaction = Emoji.fromFormatted("<:Hehe:953368285771104297>")

            event.message.addReaction(reaction).queue()

        }

    }

    fun handleMessageUpdate(channelData: ChannelData, channel: TextChannel, loggedMessage: LoggedMessage) {
        val count = channelData.counting ?: return
        if (!count.warnForEditedCounts) return

        val number = try { Expressions().eval(loggedMessage.content) }
        catch (e: Exception) { return }

        if (count.count.toBigDecimal() == number) {
            channel.sendMessage("<@!${loggedMessage.author}> thinks they're sneaky and edited their latest count, which was originally **${count.count}**").queue { warning ->
                warning.addReaction(Emoji.fromFormatted("<:warning:1084122198291271820>")).queue()
            }
        }
    }

    fun handleMessageDelete(channelData: ChannelData, channel: TextChannel, message: LoggedMessage) {
        val count = channelData.counting ?: return
        if (!count.warnForDeletedCounts) return

        val number = try { Expressions().eval(message.content) }
        catch (e: Exception) { return }

        if (count.count.toBigDecimal() == number ) {
            channel.sendMessage("<@!${message.author}> thinks they're sneaky and deleted the latest count of **${count.count}**").queue { warning ->
                warning.addReaction(Emoji.fromFormatted("<:warning:1084122198291271820>")).queue()
            }
        }
    }

}