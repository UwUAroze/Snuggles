package me.aroze.snuggles.commands.impl.generic

import me.aroze.arozeutils.minecraft.generic.uwuify
import me.aroze.snuggles.commands.BaseCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType

object UwUifyCommand : BaseCommand("uwuify", "Sends your specified message uwuified") {

    init {
        options.add(Option(
            OptionType.STRING,
            "message",
            "Specify a message to be UwUified",
            true
        ))
    }

    // Actual UwUifying is done in ArozeUtils

    override fun onExecute(event: SlashCommandEvent) {
        val message = event.getOption("message")!!.asString
            .replace("@", "`@`")
        event.reply(uwuify(message)).queue()
    }

}