package me.aroze.snuggles.commands.impl.dev

import instance
import me.aroze.snuggles.commands.DevCommand
import me.aroze.snuggles.utils.BarStyle
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class FetchGuildsCommand: DevCommand("dev guilds", "Fetch all guilds") {
    override fun onSubCommand(event: SlashCommandEvent) {

        val eb = FancyEmbed()
            .setTitle("Snuggles is in `${instance.guilds.size}` guilds!")
            .setDescription("Here's some info about them <:eyesZoom:953368332789252166>")

        for ((i, guild) in instance.guilds.withIndex()) {
            eb.addField("\u200B\n\u200B${guild.name} `${guild.id}`",
                "Is owned by ${instance.getUserById(guild.ownerId)?.asTag ?: "Unknown"} with ${guild.memberCount} members" +
                "\nGuild was created: <t:${guild.timeCreated.toEpochSecond()}:R> and Snuggles joined: <t:${guild.selfMember.timeJoined.toEpochSecond()}:R>",
            false)
        }

        event.replyEmbeds(eb.build())
            .bar(BarStyle.PINK)
            .setEphemeral(silent)
            .queue()
    }
}