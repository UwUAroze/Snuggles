package me.aroze.snuggles.commands.impl.generic

import instance
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.BaseCommand
import me.aroze.snuggles.database.database
import me.aroze.snuggles.utils.BarStyle
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar
import me.aroze.snuggles.utils.ping
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.InteractionHook
import java.time.Instant

class PingCommand : BaseCommand("ping", "Pongs") {

    override fun onExecute(event: SlashCommandEvent) {
        val now = System.currentTimeMillis()
        val timeSent = event.timeCreated.toInstant().toEpochMilli()
//        val wsPing = instance.restPing.complete()

        val description = mutableListOf(
            ":satellite: **Discord Latency**",
            " - **Gateway Latency** ${instance.gatewayPing}ms",
            " - **Rest Latency** <a:loading:1079091360046522489>",
            "",
            ":stopwatch: **Internal Latency**",
            " - **Database Latency** <a:loading:1079091360046522489>",
            "",
            " - **Total command Latency** ${now - timeSent}ms"
        )

        val eb = FancyEmbed()
            .setTitle("<:ping:1079067285945335869>  Pong!")
            .setDescription(description.joinToString("\n"))

        event.replyEmbeds(eb.build())
            .bar(BarStyle.PINK)
            .setEphemeral(silent)
            .queue { update(it, eb, description) }
    }

    private fun update(response: InteractionHook, eb: EmbedBuilder, description: MutableList<String>) = runBlocking {
        launch {
            instance.restPing.queue { restPing ->
                description[2] = " - **Rest Latency** ${restPing}ms"

                eb.setDescription(description.joinToString("\n"))
                response.editOriginalEmbeds(eb.build()).queue()

                val dbPing = database.ping()
                description[5] = " - **Database Latency** ${dbPing}ms"

                eb.setDescription(description.joinToString("\n"))
                response.editOriginalEmbeds(eb.build()).queue()
            }
        }
    }

}