package me.aroze.snuggles.command

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.Constants.SILENT_DESCRIPTION
import me.aroze.snuggles.initialisation.BotLoader.snuggles
import me.aroze.snuggles.util.BarStyle
import me.aroze.snuggles.util.bar
import me.aroze.snuggles.util.type.FancyEmbed
import me.santio.coffee.common.annotations.Command
import me.santio.coffee.common.annotations.Optional
import me.santio.coffee.jda.annotations.Description
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.InteractionHook

@Command
@Description("Pongs")
class PingCommand {

    fun ping(
        event: SlashCommandInteractionEvent,
        @Optional @Description(SILENT_DESCRIPTION) silent: Boolean?
    ) {

        val started = System.currentTimeMillis()

        val description = mutableListOf(
            ":satellite: **Discord Latency**",
            " - **Gateway Latency** ${snuggles.gatewayPing}ms",
            " - **Rest Latency** <a:loading:1079091360046522489>",
            "",
            ":stopwatch: **Internal Latency**",
            " - **Database Latency** <a:loading:1079091360046522489>",
            "",
            " - **Round Trip Command Latency** <a:loading:1079091360046522489>ms"
        )

        val eb = FancyEmbed()
            .setTitle("<:ping:1079067285945335869>  Pong!")
            .setDescription(description.joinToString("\n"))

        event.replyEmbeds(eb.build())
            .bar(BarStyle.PINK)
            .setEphemeral(silent ?: false)
            .queue { update(it, started, eb, description) }
    }

    private fun update(response: InteractionHook, started: Long, eb: EmbedBuilder, description: MutableList<String>) = runBlocking {
        val now = System.currentTimeMillis()
        launch {
            snuggles.restPing.queue { restPing ->
                description.updateLine(2, " - **Rest Latency** ${restPing}ms", eb, response)
                description.updateLine(5, " - **Database Latency** ${"[?]"}ms", eb, response) // todo: database.ping()
                description.updateLine(7, " - **Round Trip Command Latency** ${now - started}ms", eb, response)
            }
        }
    }

    private fun MutableList<String>.updateLine(line: Int, newLine: String, eb: EmbedBuilder, response: InteractionHook) {
        this[line] = newLine
        eb.setDescription(this.joinToString("\n"))
        response.editOriginalEmbeds(eb.build()).queue()
    }

}