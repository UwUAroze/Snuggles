package me.aroze.snuggles.listeners

import com.github.keelar.exprk.ExpressionException
import com.github.keelar.exprk.Expressions
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

object CountingListener: ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) = runBlocking {
        if (!event.isFromGuild) return@runBlocking
        if (event.isWebhookMessage || event.message.author.isBot) return@runBlocking

        val number = try { Expressions().eval(event.message.contentDisplay) }
        catch (e: ExpressionException) { return@runBlocking }

//        event.channel.sendMessage(number.toString()).queue()
    }

}