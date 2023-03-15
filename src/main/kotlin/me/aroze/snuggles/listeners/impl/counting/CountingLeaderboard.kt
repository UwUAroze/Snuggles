package me.aroze.snuggles.listeners.impl.counting

import me.aroze.snuggles.commands.impl.channel.counting.CountingUI
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction

object CountingLeaderboard {

    fun handleButtonPress(event: ButtonInteraction, button: String) {
        if (button == "counting-guild") {
            CountingUI.createGuildUI(event.guild!!.id).thenAccept { new ->
                event.editMessageEmbeds(new.first.build()).setActionRow(new.second).queue()
            }
            return
        }
        if (button == "counting-global") {
            CountingUI.createGlobalUI().thenAccept { new ->
                event.editMessageEmbeds(new.first.build()).setActionRow(new.second).queue()
            }
            return
        }
    }

}