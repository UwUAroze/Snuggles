package me.aroze.snuggles.commands.impl.generic

import com.github.keelar.exprk.Expressions
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.utils.BarStyle
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar

@Command(
    description = "Attempts to evaluate a given math expression. Example: (2*5)-7",
)
class EvaluateCommand {

    fun main(event: CommandEvent, expression: String) {
        val number = try { Expressions().eval(expression) }
        catch (e: Exception) {
            val eb = FancyEmbed()
                .addField("Your dumbass generated an error.", "This command is for math, not for whatever degeneracy that was.", false)

            event.message.replyEmbeds(eb.build())
                .bar(BarStyle.ERROR)
                .setEphemeral(true)
                .queue()
            return
        }

        event.message.reply("$expression = $number")
            .setEphemeral(event.silent)
            .queue()
    }

}