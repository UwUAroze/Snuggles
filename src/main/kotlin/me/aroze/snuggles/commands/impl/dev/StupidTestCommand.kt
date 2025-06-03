package me.aroze.snuggles.commands.impl.dev

import com.google.auto.service.AutoService
import me.aroze.snuggles.commands.handler.SnugglyCommand
import me.aroze.snuggles.commands.handler.silent.SilentFlag
import me.aroze.snuggles.commands.handler.silent.replySilently
import me.aroze.snuggles.constants.Emoji
import net.dv8tion.jda.api.entities.User
import org.incendo.cloud.annotation.specifier.Greedy
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.discord.jda5.JDAInteraction

@SilentFlag(true)
@AutoService(SnugglyCommand::class)
class StupidTestCommand : SnugglyCommand {

    @Command("testy <user> <message>")
    fun test(
        interaction: JDAInteraction,
        user: User,
        @Greedy message: String,
    ) {
        val event = interaction.interactionEvent()
            ?: return

        event.replySilently("<a:loading:1378828518938644520> <:loading:1378828518938644520> ${Emoji.Animated.LOADING}")
            .queue()
    }

}
