package me.aroze.snuggles.listeners

import com.github.keelar.exprk.Expressions
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.models.CountData
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object CountingListener: ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) = runBlocking {
        if (!event.isFromGuild) return@runBlocking
        if (event.isWebhookMessage || event.message.author.isBot) return@runBlocking

        val number = try { Expressions().eval(event.message.contentDisplay) }
        catch (e: Exception) { return@runBlocking }

        launch {
            val count = CountData.get(event.channel.id) ?: return@launch

            if (number.toInt() != count.count + 1) {
                event.message.addReaction(Emoji.fromFormatted("<a:bonk:1081320319500955718>")).queue()
                event.message.channel.sendMessage("You're so fucking stupid, ${event.author.asMention}. Go back to school.").queue()
                count.count = 0
                return@launch
            }

            count.count++
            event.message.addReaction(Emoji.fromFormatted("<:yes:953661030268022795>")).queue()

        }
    }

}