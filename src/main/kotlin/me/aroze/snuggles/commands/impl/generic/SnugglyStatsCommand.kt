package me.aroze.snuggles.commands.impl.generic

import instance
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.utils.FancyEmbed
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.interactions.components.Button

@Command(
    description = "Some fun little global stats about Snuggles"
)
class SnugglyStatsCommand {
    fun main(event: CommandEvent) {
        val eb = FancyEmbed()
            .setTitle("Snuggly Stats!")
            .setDescription("Some fun stats and info about Snuggles!")
            .addField("Server count", instance.guilds.size.toString(), true)
            .addField("Total slash command executions", Database.botStats.totalExecutions.toString(), true)

        event.message.replyEmbeds(eb.build())
            .setEphemeral(event.silent)
            .addActionRow(
                Button.link("https://github.com/UwUAroze/Snuggles", "I'm open sourced!")
                    .withEmoji(Emoji.fromMarkdown("<:github:1077388578511917167>")),
            )
            .queue()

    }
}