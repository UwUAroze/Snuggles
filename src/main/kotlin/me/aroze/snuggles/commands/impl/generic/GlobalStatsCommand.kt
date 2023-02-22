package me.aroze.snuggles.commands.impl.generic

import jda
import me.aroze.snuggles.commands.BaseCommand
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.components.Button

object GlobalStatsCommand : BaseCommand("globalstats", "Some fun little global stats about Snuggles") {

    override fun onExecute(event: SlashCommandEvent) {

        val eb = EmbedBuilder()
        eb.setTitle("Snuggles • Global Stats")
        eb.setDescription("Here's some fun little global stats about Snuggles!")
        eb.setColor(0xFFBAD3)
        eb.addField("Server count", jda.guilds.size.toString(), true)
        eb.setFooter("Snuggles • Made with ♥ by Aroze#0001")

        event.replyEmbeds(eb.build())
            .addActionRow(
                Button.link("https://github.com/UwUAroze/Snuggles", "I'm open sourced!")
                    .withEmoji(Emoji.fromMarkdown("<:github:1077388578511917167>")),
            )
            .queue()

    }

}