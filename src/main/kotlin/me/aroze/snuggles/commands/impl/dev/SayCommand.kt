package me.aroze.snuggles.commands.impl.dev

import me.aroze.snuggles.commands.DevCommand
import me.aroze.snuggles.utils.BarStyle
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType

class SayCommand : DevCommand("dev say", "Make Snuggles say something") {

    init {
        options.add(Option(
            OptionType.STRING,
            "message",
            "The message to make Snuggles say",
            true
        ))
    }
    override fun onSubCommand(event: SlashCommandEvent) {
        val eb = FancyEmbed()
            .setDescription("Okay! <:Hehe:953368285771104297>")
        event.replyEmbeds(eb.build()).setEphemeral(silent).queue()

        event.channel.sendMessage(event.getOption("message")!!.asString).queue()
    }

}