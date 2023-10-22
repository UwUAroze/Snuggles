package me.aroze.snuggles.initialisation

import me.aroze.snuggles.config.ConfigLoader.config
import me.santio.coffee.common.Coffee
import me.santio.coffee.jda.CoffeeJDA
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.messages.MessageRequest

object BotLoader {

    lateinit var snuggles: JDA

    fun initialise() {
        login()
        registerCommands()
    }

    private fun login() {
        snuggles = JDABuilder
            .createDefault(config.authentication.token)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .build()
            .awaitReady()

        MessageRequest.setDefaultMentions(listOf(Message.MentionType.USER, Message.MentionType.CHANNEL, Message.MentionType.EMOJI))
    }

    private fun registerCommands() {
        snuggles.updateCommands().complete()

        Coffee.verbose()
        Coffee.import(CoffeeJDA(snuggles))
        Coffee.brew("me.aroze.snuggles.command")
    }

}