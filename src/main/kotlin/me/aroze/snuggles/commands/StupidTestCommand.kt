package me.aroze.snuggles.commands

import com.google.auto.service.AutoService
import me.aroze.snuggles.commands.handler.silent.SilentFlag
import me.aroze.snuggles.commands.handler.silent.replySilently
import net.dv8tion.jda.api.entities.User
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.discord.jda5.JDAInteraction

//@SilentFlag(false)
@AutoService(SnugglyCommand::class)
class StupidTestCommand : SnugglyCommand {

    @Command("testy <user> <message>")
    fun test(
        interaction: JDAInteraction,
        user: User,
        message: String,
    ) {
        val event = interaction.interactionEvent()
            ?: return

        event.replySilently("Hello ${user.asMention}, you said: $message")
            .queue()
    }

}
