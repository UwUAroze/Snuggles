package me.aroze.snuggles.commands.impl.generic

import instance
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.getAllUsers
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.buttons.Button
import startTime

@Command(
    description = "Some fun little global stats about Snuggles"
)
class SnugglyStatsCommand {
    fun main(event: CommandEvent) {

        val owner = instance.getUserById("273524398483308549")!!
        var ownerString = "`${owner.asTag}` (${owner.id})"

        val ownerGuilds = owner.mutualGuilds
        val mutualGuilds = event.message.user.mutualGuilds

        if (ownerGuilds.intersect(mutualGuilds.toSet()).isNotEmpty()) ownerString = owner.asMention

        val eb = FancyEmbed()
            .setTitle("Snuggles...")
            .setDescription(
                "\n" +
                " > ...is maintained by ${ownerString}\n" +
                " > ...is in **${instance.guilds.size}** servers, watching over **${instance.getAllUsers()}** users\n" +
                " > ...has been used to execute **${Database.botStats.totalExecutions}** commands\n" +
                " > ...has accepted **${Database.botStats.totalCounts}** instances of counting\n" +
                " > ...was last started <t:${(startTime / 1000)}:R>\n"
            )

        event.message.replyEmbeds(eb.build())
            .setEphemeral(event.silent)
            .addActionRow(
                Button.link("https://github.com/UwUAroze/Snuggles", "I'm open sourced!")
                    .withEmoji(Emoji.fromFormatted("<:github:1077388578511917167>")),
                Button.link("https://discord.com/api/oauth2/authorize?client_id=1071500899186389132&permissions=140123688001&scope=bot%20applications.commands",
                    "Invite Me Bitch!")
                    .withEmoji(Emoji.fromFormatted("<:plussy:1082129739680067704>"))
            )
            .queue()

    }
}