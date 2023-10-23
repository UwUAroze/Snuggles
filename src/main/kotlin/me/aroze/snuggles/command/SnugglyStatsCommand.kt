package me.aroze.snuggles.command

import jdk.jfr.Name
import me.aroze.arozeutils.kotlin.extension.Number.formatCommas
import me.aroze.snuggles.config.ConfigLoader.config
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.database.Database.database
import me.aroze.snuggles.database.models.SnugglyStats
import me.aroze.snuggles.initialisation.BotLoader.owner
import me.aroze.snuggles.initialisation.BotLoader.snuggles
import me.aroze.snuggles.initialisation.BotLoader.startTime
import me.aroze.snuggles.util.getAllUsers
import me.aroze.snuggles.util.type.FancyEmbed
import me.santio.coffee.common.annotations.Command
import me.santio.coffee.common.annotations.Optional
import me.santio.coffee.jda.annotations.Description
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

@Command("snugglystats")
@Description("Some fun little global stats about Snuggles")
class SnugglyStatsCommand {

    fun snugglyStats(
        event: SlashCommandInteractionEvent,
        @Optional @Description("Should this only show to you?") silent: Boolean?
    ) {

        val ownerString = if (owner.mutualGuilds.intersect(event.user.mutualGuilds.toSet()).isNotEmpty())
            owner.asMention else "`@${owner.name}` (${owner.id})"

        val eb = FancyEmbed()
            .setTitle("Snuggles...")
            .setDescription("""
                > ...is maintained by $ownerString
                > ...is in **${snuggles.guilds.size.formatCommas(false)}** servers, watching over **${snuggles.getAllUsers().formatCommas(false)}** users
                > ...has been used to execute **${Database.snugglyStats.totalExecutions.formatCommas(false)}** commands
                > ...has accepted **${Database.snugglyStats.totalCounts.formatCommas(false)}** instances of counting
                > ...was last started <t:${(startTime / 1000)}:R>
            """.trimIndent())

        event.replyEmbeds(eb.build())
            .setEphemeral(silent ?: false)
            .addActionRow(
                Button.link("https://github.com/UwUAroze/Snuggles", "I'm open source!")
                    .withEmoji(Emoji.fromFormatted("<:github:1077388578511917167>")),
                Button.link(config.bot.inviteLink,
                    "Invite Me Bitch!")
                    .withEmoji(Emoji.fromFormatted("<:plussy:1082129739680067704>")),
            )
            .queue()

    }

}