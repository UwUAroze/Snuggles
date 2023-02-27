package me.aroze.snuggles.commands.impl.generic

import instance
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.database.database
import me.aroze.snuggles.utils.BarStyle
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar
import me.aroze.snuggles.utils.ping
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.interactions.InteractionHook

@Command(
    description = "Pongs"
)
class PingCommand {
    fun main(event: CommandEvent) {
        val now = System.currentTimeMillis()
        val timeSent = event.message.timeCreated.toInstant().toEpochMilli()
//        val wsPing = instance.restPing.complete()

        val description = mutableListOf(
            ":satellite: **Discord Latency**",
            " - **Gateway Latency** ${instance.gatewayPing}ms",
            " - **Rest Latency** <a:loading:1079091360046522489>",
            "",
            ":stopwatch: **Internal Latency**",
            " - **Database Latency** <a:loading:1079091360046522489>",
            "",
            " - **Total Command Latency** ${now - timeSent}ms"
        )

        val eb = FancyEmbed()
            .setTitle("<:ping:1079067285945335869>  Pong!")
            .setDescription(description.joinToString("\n"))

        event.message.replyEmbeds(eb.build())
            .bar(BarStyle.PINK)
            .setEphemeral(event.silent)
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