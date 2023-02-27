package me.aroze.snuggles.initialisation

import instance
import me.aroze.snuggles.config.ConfigLoader
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.utils.AllowedMentions

object Login {

    fun login() {
        instance = JDABuilder
            .createDefault(ConfigLoader.config.getString("authentication.token"))
            .build()

        AllowedMentions.setDefaultMentions(listOf(Message.MentionType.USER, Message.MentionType.CHANNEL, Message.MentionType.EMOTE))
    }

}