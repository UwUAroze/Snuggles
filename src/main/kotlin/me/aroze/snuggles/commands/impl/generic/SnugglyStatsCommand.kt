package me.aroze.snuggles.commands.impl.generic

import instance
import me.aroze.snuggles.commands.BaseCommand
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.components.Button

class SnugglyStatsCommand : BaseCommand("snugglystats", "Some fun little global stats about Snuggles") {

    override fun onExecute(event: SlashCommandEvent) {

        val eb = FancyEmbed()
            .setTitle("Snuggles • Global Stats")
            .setDescription("Here's some fun little global stats about Snuggles!")
            .addField("Server count", instance.guilds.size.toString(), true)
            .addField("Total slash command executions", Database.botStats.totalExecutions.toString(), true)

        event.replyEmbeds(eb.build())
            .bar()
            .setEphemeral(silent)
            .addActionRow(
                Button.link("https://github.com/UwUAroze/Snuggles", "I'm open sourced!")
                    .withEmoji(Emoji.fromMarkdown("<:github:1077388578511917167>")),
            )
            .queue()

    }

}