package me.aroze.snuggles.listeners

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.impl.settings.CountingCommand
import me.aroze.snuggles.models.CountData
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow


object SelectionListener : ListenerAdapter() {

    override fun onEntitySelectInteraction(event: EntitySelectInteractionEvent) = runBlocking {
        launch {
            if (event.componentId == "counting-channels") {
                val options = event.mentions.channels
                val count = if (options.isEmpty()) {
                    val data = CountData.getByGuild(event.guild!!.id)
                    data?.disabled = true
                    null
                } else CountData.create(options.first().id, event.guild!!.id)

                val replyData = CountingCommand.getCountingUI(count)
                event.interaction.editMessageEmbeds(replyData.embed.build())
                    .setComponents(ActionRow.of(replyData.channelSelection), ActionRow.of(replyData.settingsSelection))
                    .queue()
            }
        }
        Unit
    }

    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) = runBlocking {
        launch {
            val count = CountData.getByGuild(event.guild!!.id)!!

            if (event.componentId == "counting-settings") {
                val settings = event.values
                count.allowConsecutiveUsers = settings.contains("consecutive-counting")
                count.allowTalking = settings.contains("allow-speaking")

                val replyData = CountingCommand.getCountingUI(count)
                event.interaction.editMessageEmbeds(replyData.embed.build())
                    .setComponents(ActionRow.of(replyData.channelSelection), ActionRow.of(replyData.settingsSelection))
                    .queue()
            }
        }
        Unit
    }

}