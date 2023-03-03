package me.aroze.snuggles.commands.impl.settings

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.models.CountData
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel

@Command(
    "counting",
    "Settings for counting"
)
class CountingCommand {

    @Command(description = "Set the channel for counting")

    fun add(event: CommandEvent, channel: GuildChannel?) = runBlocking {
        val channy = channel ?: event.message.channel

        if (channy !is TextChannel) {
            event.message.reply("You must provide a text channel").queue()
            return@runBlocking
        }

        launch {
            CountData.create(channy.id, channy.guild.id)
            event.message.reply("Counting channel set to ${channy.asMention}").queue()
        }

    }

    fun remove(event: CommandEvent, channel: GuildChannel?) = runBlocking {
        val channy = channel ?: event.message.channel

        if (channy !is TextChannel) {
            event.message.reply("You must provide a text channel").queue()
            return@runBlocking
        }

        launch {
            CountData.get(channy.id)?.delete() ?: let {
                event.message.reply("${channy.asMention} has not been added as a counting channel").queue()
                return@launch
            }

            event.message.reply("Counting channel removed from ${channy.asMention}").queue()
        }

    }

}