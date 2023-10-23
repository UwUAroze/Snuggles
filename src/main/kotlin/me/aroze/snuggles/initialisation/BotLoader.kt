package me.aroze.snuggles.initialisation

import me.aroze.arozeutils.kotlin.reflection.getClassesInPackage
import me.aroze.snuggles.config.ConfigLoader.config
import me.aroze.snuggles.listener.SlashCommandListener
import me.santio.coffee.common.Coffee
import me.santio.coffee.jda.CoffeeJDA
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.messages.MessageRequest
import org.reflections.Reflections

object BotLoader {

    val startTime = System.currentTimeMillis()

    lateinit var snuggles: JDA
    lateinit var owner: User

    fun initialise() {
        login()
        registerCommands()

        owner = snuggles.retrieveUserById("273524398483308549").complete()!!
    }

    private fun login() {
        snuggles = JDABuilder
            .createDefault(config.authentication.token)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
//            .addEventListeners(Reflections("me.aroze.snuggles.listener").getSubTypesOf(ListenerAdapter::class.java).map { it.getDeclaredConstructor().newInstance() })
            .addEventListeners(SlashCommandListener())
            .build()
            .awaitReady()

        MessageRequest.setDefaultMentions(listOf(Message.MentionType.USER, Message.MentionType.CHANNEL, Message.MentionType.EMOJI))
    }

    private fun registerCommands() {
        snuggles.updateCommands().complete()

        Coffee.import(CoffeeJDA(snuggles))
        Coffee.brew("me.aroze.snuggles.command")
    }

}