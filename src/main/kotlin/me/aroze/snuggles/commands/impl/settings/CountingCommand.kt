package me.aroze.snuggles.commands.impl.settings

import instance
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.commands.handler.Ignore
import me.aroze.snuggles.models.CountData
import me.aroze.snuggles.utils.FancyEmbed
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

@Command(
    "counting",
    "Settings for counting",
    silentToggle = false
    )
class CountingCommand {

    @Command(
        description = "Settings for the counting module.",
        permissions = [Permission.MANAGE_CHANNEL],
        silentToggle = false,
    )
    fun settings(event: CommandEvent) = runBlocking {
        launch {
            val count = CountData.getByGuild(event.message.guild!!.id)
            val replyData = getCountingUI(count)

            event.message.replyEmbeds(replyData.embed.build())
                .addActionRow(replyData.channelSelection)
                .addActionRow(replyData.settingsSelection)
                .setEphemeral(true)
                .queue()
        }
    }

    companion object {
        @Ignore
        fun getCountingUI(count: CountData?) : CountingSettingData {

            val channelSelection = EntitySelectMenu.create("counting-channels", EntitySelectMenu.SelectTarget.CHANNEL)
                .setChannelTypes(ChannelType.TEXT)
                .setPlaceholder("Select a channel for counting to take place in.")
                .setRequiredRange(0, 1)

            val settingsSelection = StringSelectMenu.create("counting-settings")
                .setPlaceholder("Counting settings (select a channel first!)")
                .setRequiredRange(0, 25)
                .setDisabled(true)
                .addOption("Consecutive counting", "consecutive-counting", "Allows for the same user to count multiple times in a row.")
                .addOption("Allow speaking", "allow-speaking", "Allows for users to speak in the counting channel.")

            if (count != null && !count.disabled) {
                val channel = instance.getGuildChannelById(count.id)
                settingsSelection
                    .setDefaultOptions(count.getSelectedOptions())
                    .setDisabled(false)
                    .placeholder = "Toggle settings for the counting module."
                channelSelection
                    .placeholder = "Select a channel. Current: #${channel?.name ?: "deleted channel"}"
            }

            val eb = FancyEmbed()
                .setTitle("Counting Settings <:flushed_cool:1081364373295087686>")
                .setDescription(
                    "The menu below is *very self explanatory*. But if ur stupid ass needs help, I got u <3\n\n" +
                    "The thingy that says `select a channel` is to select a channel. If the server has " +
                    "a channel selected, the other thingy that says `toggle settings` becomes usable, " +
                    "and allows you to, you guessed it, toggle settings.\n\n" +
                    "...You're fucking welcome"
                )

            return CountingSettingData(eb, channelSelection.build(), settingsSelection.build())
        }
    }

    @Ignore
    data class CountingSettingData(
        val embed: EmbedBuilder,
        val channelSelection: EntitySelectMenu,
        val settingsSelection: StringSelectMenu
    )

}