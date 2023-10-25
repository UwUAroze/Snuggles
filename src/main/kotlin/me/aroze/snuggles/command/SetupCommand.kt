package me.aroze.snuggles.command

import me.aroze.snuggles.setup.GenericSetup
import me.santio.coffee.common.annotations.Command
import me.santio.coffee.jda.annotations.Description
import me.santio.coffee.jda.annotations.Permission
import me.santio.coffee.jda.gui.button.Button
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle

import net.dv8tion.jda.api.Permission as JDAPermission

@Command
@Permission(JDAPermission.MANAGE_CHANNEL)
@Description("Setup different features of Snuggles.")
class SetupCommand {

    @Description("Setup or change settings relating to logging")
    fun logging(event: SlashCommandInteractionEvent) {

        GenericSetup("Logging")
            .addOption("Log message changes", "Sends a log whenever a message is edited or deleted.") { println(it) }
            .send(event)
            .queue()
    }

    @Description("Setup or change settings relating to counting")
    fun counting(event: SlashCommandInteractionEvent) {
        event.reply("b").queue()
    }

}