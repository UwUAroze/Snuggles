package me.aroze.snuggles.commands.impl.feelings

import me.aroze.snuggles.commands.feelings.Feelings
import me.aroze.snuggles.commands.feelings.FeelingsManager
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import net.dv8tion.jda.api.entities.User

@Command(
    description = "slaps your victim",
)
class SlapCommand {
    fun main(event: CommandEvent, target: User) {
        FeelingsManager.handleFeeling(Feelings.SLAP, event)
    }
}