package me.aroze.snuggles.commands.impl.dev

import instance
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.utils.BarStyle
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar

@Command(
    description = "Developer command for Snuggles",
    devOnly = true,
    defaultSilent = true,
)
class DevCommand {
    @Command(description = "Make Snuggles say something")
    fun say(event: CommandEvent, message: String) {
        val eb = FancyEmbed()
            .setDescription("Okay! <:Hehe:953368285771104297>")
        event.message.replyEmbeds(eb.build()).setEphemeral(event.silent).queue()

        event.message.channel.sendMessage(message).queue()
    }

    @Command(description = "Fetch all guilds")
    fun guilds(event: CommandEvent) {
        val eb = FancyEmbed()
            .setTitle("Snuggles is in `${instance.guilds.size}` guilds!")
            .setDescription("Here's some info about them <:eyesZoom:953368332789252166>")

        for ((i, guild) in instance.guilds.withIndex()) {
            eb.addField("\u200B\n\u200B${guild.name} `${guild.id}`",
                "Is owned by ${instance.getUserById(guild.ownerId)?.asTag ?: "Unknown"} with ${guild.memberCount} members" +
                    "\nGuild was created: <t:${guild.timeCreated.toEpochSecond()}:R> and Snuggles joined: <t:${guild.selfMember.timeJoined.toEpochSecond()}:R>",
                false)
        }

        event.message.replyEmbeds(eb.build())
            .bar(BarStyle.PINK)
            .setEphemeral(event.silent)
            .queue()
    }
}