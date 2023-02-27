package me.aroze.snuggles.commands.impl.generic

import me.aroze.arozeutils.minecraft.generic.uwuify
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent

@Command(
    name = "uwuify",
    description = "UwUify a message"
)
class UwUifyCommand {
    fun main(event: CommandEvent, message: String) {
        event.message.reply(uwuify(message.replace("@", "`@`"))).setEphemeral(event.silent).queue()
    }
}