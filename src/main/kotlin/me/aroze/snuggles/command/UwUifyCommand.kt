package me.aroze.snuggles.command

import me.aroze.arozeutils.minecraft.generic.text.uwuify
import me.aroze.snuggles.Constants.SILENT_DESCRIPTION
import me.santio.coffee.common.annotations.Command
import me.santio.coffee.common.annotations.Optional
import me.santio.coffee.jda.annotations.Description
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

@Command("uwuify")
@Description("UwUify a message")
class UwUifyCommand {

    fun uwuify(
        event: SlashCommandInteractionEvent,
        @Description(SILENT_DESCRIPTION) message: String,
        @Optional @Description("Should this only show to you? (it won't by default!)") silent: Boolean?
    ) {
        // uwu ;3 -santio
        event.reply(uwuify(message)).setEphemeral(silent ?: false).queue()
    }

}