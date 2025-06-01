package me.aroze.snuggles.commands.impl.utility

import com.google.auto.service.AutoService
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.handler.SnugglyCommand
import me.aroze.snuggles.commands.handler.silent.SilentFlag
import me.aroze.snuggles.commands.handler.silent.replyEmbedsSilently
import me.aroze.snuggles.constants.Emoji
import me.aroze.snuggles.database
import me.aroze.snuggles.database.ping
import me.aroze.snuggles.snuggles
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.InteractionHook
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.discord.jda5.JDAInteraction
import kotlin.collections.joinToString

// todo: implement components v2 and make this prettier. waiting on jda :sob:
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
            " - **Gateway Latency** ${snuggles.gatewayPing}ms",
            " - **Rest Latency** ${Emoji.Animated.LOADING}",
            "",
            ":stopwatch: **Internal Latency**",
            " - **Database Latency** ${Emoji.Animated.LOADING}",
            "",
            " - **Total Command Latency** ${now - timeSent}ms"
        )

        val eb = EmbedBuilder()
            .setTitle("${Emoji.Animated.DRUGGED_PING}  Pinging...")
            .setDescription(description.joinToString("\n"))

        event.replyEmbedsSilently(eb.build())
            .queue() { response -> updateRestPing(response, eb, description) }
    }

    private fun updateRestPing(response: InteractionHook, eb: EmbedBuilder, description: MutableList<String>) {
        snuggles.restPing.queue() { restPing ->
            description[2] = " - **Rest Latency** ${restPing}ms"
            eb.setDescription(description.joinToString("\n"))
            response.editOriginalEmbeds(eb.build()).queue() { response -> updateDatabasePing(response, eb, description) }
        }
    }

    private fun updateDatabasePing(response: Message, eb: EmbedBuilder, description: MutableList<String>) {
        runBlocking {
            description[5] = " - **Database Latency** ${database.ping()}ms"
            eb.setDescription(description.joinToString("\n"))
            response.editMessageEmbeds (eb.build()).queue()
        }
    }

}
