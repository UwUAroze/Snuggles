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

            if (number.toInt() != count.count + 1) {
                event.message.addReaction(Emoji.fromFormatted("<a:bonk:1081320319500955718>")).queue()
                event.message.channel.sendMessage("You're so fucking stupid, ${event.author.asMention}. Go back to school.").queue()
                count.resetCurrentCount()
                return@launch
            }

            if (!count.allowConsecutiveUsers && count.lastCounter == event.author.id) {
                event.message.addReaction(Emoji.fromFormatted("<a:bonk:1081320319500955718>")).queue()
                event.message.channel.sendMessage("You ruined it ${event.author.asMention}! You aren't allowed to type two numbers in a row.").queue()
                count.resetCurrentCount()
                return@launch
            }

            Database.botStats.totalCounts++
            count.count++
            count.lastCounter = event.author.id
            val highScore = count.count > count.highScore

            if (highScore) count.highScore = count.count

            var reaction = Emoji.fromFormatted("<:tick_pink:1081999667287568384>")
            if (highScore) reaction = Emoji.fromFormatted("<:tick_viola:1082002568005296279>")
            if (count.count == 100) reaction = Emoji.fromFormatted(":100:")
            if (count.count.toString().contains("69")) reaction = Emoji.fromFormatted("<:Hehe:953368285771104297>")

            event.message.addReaction(reaction).queue()

        }
    }

}