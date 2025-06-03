package me.aroze.snuggles.commands.impl.utility

import com.google.auto.service.AutoService
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.handler.SnugglyCommand
import me.aroze.snuggles.commands.handler.silent.SilentFlag
import me.aroze.snuggles.commands.handler.silent.replyComponentsSilently
import me.aroze.snuggles.constants.BarColor
import me.aroze.snuggles.constants.Emoji
import me.aroze.snuggles.database
import me.aroze.snuggles.database.ping
import me.aroze.snuggles.snuggles
import me.aroze.snuggles.ui.img.coloredBar
import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.components.textdisplay.TextDisplay
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.InteractionHook
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.discord.jda5.JDAInteraction


@AutoService(SnugglyCommand::class)
class PingCommand : SnugglyCommand {

    @Command("ping")
    @SilentFlag()
    fun ping(
        interaction: JDAInteraction
    ) {
        val event = interaction.interactionEvent()
            ?: return

        val now = System.currentTimeMillis()
        val timeSent = event.timeCreated.toInstant().toEpochMilli()

        val description = mutableListOf(
            ":satellite: **Discord Latency**",
            "> Gateway: `${snuggles.gatewayPing}ms`",
            "> REST: ${Emoji.Animated.LOADING}",
            "",
            ":stopwatch: **Internal Latency**",
            "> **Database** ${Emoji.Animated.LOADING}",
            "",
            "\uD83E\uDDEE  Total Command Latency: `${now - timeSent}ms`"
        )

        val container = constructPingResponseComponent(description, false)

        event.replyComponentsSilently(container)
            .useComponentsV2()
            .queue() { response -> updateRestPing(response, description) }

    }

    private fun constructPingResponseComponent(description: MutableList<String>, finished: Boolean): Container = Container.of(
        if (finished) TextDisplay.of("## ${Emoji.Static.PING}  Pong!")
        else TextDisplay.of("## ${Emoji.Animated.DRUGGED_PING}  Pinging..."),

        TextDisplay.of(description.joinToString("\n")),

        coloredBar(BarColor.PINK, 230, 5)
    )

    private fun updateRestPing(response: InteractionHook, description: MutableList<String>) {
        snuggles.restPing.queue() { restPing ->
            description[2] = "> REST: `${restPing}ms`"
            val newContainer = constructPingResponseComponent(description, false)
            response.editOriginalComponents(newContainer)
                .useComponentsV2()
                .queue() { response -> updateDatabasePing(response, description) }
        }
    }

    private fun updateDatabasePing(response: Message, description: MutableList<String>) {
        runBlocking {
            description[5] = "> Database: `${database.ping()}ms`"
            val newContainer = constructPingResponseComponent(description, true)
            response.editMessageComponents(newContainer)
                .useComponentsV2()
                .queue()
        }
    }

}
