package me.aroze.snuggles.commands.impl.settings

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.models.CountData
import me.aroze.snuggles.utils.FancyEmbed
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

            val channelSelection = EntitySelectMenu.create("counting-channels", EntitySelectMenu.SelectTarget.CHANNEL)
                .setChannelTypes(ChannelType.TEXT)
                .setPlaceholder("Select a channel for counting to take place in.")
                .setRequiredRange(0, 1)
                .build()

            val settingsSelection = StringSelectMenu.create("counting-settings")
                .setPlaceholder("Toggle settings for the counting module.")
                .setRequiredRange(0, 25)
                .addOption("Consecutive counting", "consecutive-counting", "Allows for the same user to count multiple times in a row.")
                .addOption("Allow speaking", "allow-speaking", "Allows for users to speak in the counting channel.")

            if (count != null) settingsSelection.setDefaultOptions(count.getSelectedOptions())


            val eb = FancyEmbed()
                .setTitle("Counting Settings <:flushed_cool:1081364373295087686>")
                .setDescription(
                    "The menu's below should be really self explanatory.\n" +
                        "BUT. If your stupid ass needs help, don't worry. I got you \n\n" +
                        "The thingy that says `select a channel`, is to select a channel\n" +
                        "And the other thingy that says `toggle settings`, is to toggle settings.\n\n" +
                        "You're fucking welcome."
                )

            event.message.replyEmbeds(eb.build())
                .addActionRow(channelSelection)
                .addActionRow(settingsSelection.build())
                .setEphemeral(true)
                .queue()


        }
    }

}