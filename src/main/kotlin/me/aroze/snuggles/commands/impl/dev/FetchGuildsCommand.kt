package me.aroze.snuggles.commands.impl.dev

import instance
import me.aroze.snuggles.commands.DevCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class FetchGuildsCommand: DevCommand("dev guilds", "Fetch all guilds") {
    override fun onSubCommand(event: SlashCommandEvent) {
        val sb = StringBuilder()
        instance.guilds.forEach { guild ->
            sb.append("\n\n**${guild.name}** (`${guild.id}`)")
                .append("\nOwner: ${instance.getUserById(guild.ownerId)?.asTag ?: "Unknown"}")
                .append("\nMembers: ${guild.memberCount}")
        }

        event.reply(sb.toString()).setEphemeral(silent).queue()
    }
}