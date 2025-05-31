package me.aroze.snuggles.commands

import com.google.auto.service.AutoService
import net.dv8tion.jda.api.entities.User
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.discord.jda5.JDAInteraction

@AutoService(SnugglyCommand::class)
class StupidTestCommand : SnugglyCommand {

    @Command("test <user> <message>")
    fun test(
        interaction: JDAInteraction,
        user: User,
        message: String
    ) {
        val event = interaction.interactionEvent()
            ?: return

        event.reply("Hello ${user.asMention}, you said: $message")
            .setEphemeral(true)
            .queue()
    }

}
