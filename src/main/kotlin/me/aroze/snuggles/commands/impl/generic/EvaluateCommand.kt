package me.aroze.snuggles.commands.impl.generic

import com.github.keelar.exprk.Expressions
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent

@Command(
    description = "Attempts to evaluate a given math expression. Example: (2*5)-7",
)
class EvaluateCommand {

    fun main(event: CommandEvent, expression: String) {
        val number = try { Expressions().eval(expression) }
        catch (e: Exception) {
            return
        }

        event.message.reply("$expression = $number").queue()
    }

}