package me.aroze.snuggles.commands.impl.generic

import me.aroze.snuggles.commands.handler.Autocomplete
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent

@Command(
    name = "test",
    description = "testy test test"
)
class TestCommand {

    fun main(event: CommandEvent,
         @Autocomplete([
             "Cat",
             "Dog",
             "Aroze",
         ], force = true) fruity: String
    ) {
        event.message.reply("You chose $fruity").queue()
    }

}