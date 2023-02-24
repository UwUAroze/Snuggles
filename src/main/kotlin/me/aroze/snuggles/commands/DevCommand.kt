package me.aroze.snuggles.commands

import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.utils.BarStyle
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

abstract class DevCommand(name: String, val description: String) : BaseCommand(name, description) {

    override val devOnly = true
    open override val defaultSilent = true
    val info = name.split(" ").map { it.lowercase() }

    abstract fun onSubCommand(event: SlashCommandEvent)

    override fun onExecute(event: SlashCommandEvent) {

        if (!ConfigLoader.config.developers.contains(event.user.id)) {
            val eb = FancyEmbed()
                .addField("Woah there, slow down", "This command is only for Snuggles devs, and you are no developer!", false)

            event.replyEmbeds(eb.build())
                .bar(BarStyle.ERROR)
                .setEphemeral(true)
                .queue()

            return
        }

        if (info.size != 2) return
        if (event.subcommandName == null) return
        if (event.subcommandName!!.lowercase() != info[1]) return

        onSubCommand(event)
    }

}