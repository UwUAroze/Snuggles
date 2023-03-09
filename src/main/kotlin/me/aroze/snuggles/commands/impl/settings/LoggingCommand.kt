package me.aroze.snuggles.commands.impl.settings

import instance
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.menu.ChannelMenu
import me.aroze.snuggles.menu.MenuOption
import me.aroze.snuggles.models.ChannelData
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

@Command(
    "logging",
    "Settings for logging",
    silentToggle = false
)
class LoggingCommand {

    @Command(
        description = "Settings for the logging module.",
        permissions = [Permission.MANAGE_CHANNEL],
        silentToggle = false,
    )
    fun settings(event: CommandEvent) = runBlocking {
        launch {
            val channelData = ChannelData.getByChannel(event.message.channel.id)
            var log = channelData?.logging
            val channel = channelData?.channel?.let { instance.getGuildChannelById(it) } as? TextChannel

            ChannelMenu("Logging") {

                log?.logMessageChanges = it.settings.contains("log_message_changes")

                if (it.channel == null) {
                    log?.disabled = true
                } else log = ChannelData.create(it.channel.id, it.channel.guild.id).createLogging()
            }
                .addOption(MenuOption("Log message changes", "Sends a log whenever a message is edited or deleted.", true))
                .setChannel(channel)
                .setSelectedOptions(log?.getSelectedOptions())
                .send(event.message)
        }
    }
}