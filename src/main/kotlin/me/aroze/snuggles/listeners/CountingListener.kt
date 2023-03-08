package me.aroze.snuggles.listeners

import com.github.keelar.exprk.Expressions
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.models.CountData
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object CountingListener: ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) = runBlocking {
        if (!event.isFromGuild) return@runBlocking
        if (event.isWebhookMessage || event.message.author.isBot) return@runBlocking

        launch {
            val count = CountData.getByChannel(event.channel.id) ?: return@launch

            val number = try { Expressions().eval(event.message.contentDisplay) }
            catch (e: Exception) {
                if (!count.allowTalking) event.message.delete().queue()
                return@launch
            }

            val beNice = count.kinderMessages

            if (number.toInt() != count.count + 1) {
                event.message.addReaction(Emoji.fromFormatted("<a:bonk:1081320319500955718>")).queue()
                event.message.channel.sendMessage(
                    if (event.author.id = 1058554860821282977) "Everyone look, this pedo ${event.author.asMention} FUCKED IT UP at **${count.count}**. I would say go back to school, BUT YOU CANT GO WITHIN 100 FEET OF ONE."
                    else if (beNice) "${event.author.asMention} ruined it at **${count.count}**, I authorize you all to yell at them, but be nice!"
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

            Database.botStats.totalCounts++
            count.count++
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

}
