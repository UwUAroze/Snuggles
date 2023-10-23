package me.aroze.snuggles.listener

import me.aroze.snuggles.database.Database.snugglyStats
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.ListenerAdapter

class SlashCommandListener: ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        snugglyStats.totalExecutions++
    }

}