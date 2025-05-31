package me.aroze.snuggles.initialisation

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.messages.MessageRequest

class BotLoader(
    private val token: String
) {

    fun loadDefault(): JDA {
        val bot = setupJDA()
        MessageRequest.setDefaultMentions(listOf(Message.MentionType.USER, Message.MentionType.CHANNEL, Message.MentionType.EMOJI))
        registerCommands()
        return bot
    }

    private fun setupJDA(): JDA {
        return JDABuilder
            .createDefault(token)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .build()
            .awaitReady()
    }

    private fun registerCommands() {
        // cloud shit
    }

}
