package me.aroze.snuggles.listeners

import me.aroze.snuggles.models.CountData
import me.aroze.snuggles.utils.FancyEmbed
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


object SelectionListener : ListenerAdapter() {

    override fun onEntitySelectInteraction(event: EntitySelectInteractionEvent) = runBlocking {
        launch {
            if (event.componentId == "counting-channels") {
                val channel = event.mentions.channels.first()
                CountData.create(channel.id, event.guild!!.id)
                println("Counting channel set to ${channel.name}")

                val eb = FancyEmbed()
                    .setDescription("This server's counting channel has been set to ${channel.asMention}")

                event.replyEmbeds(eb.build())
                    .setEphemeral(true)
                    .queue()

            }
        }
        Unit
    }

}