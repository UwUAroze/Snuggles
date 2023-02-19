import me.aroze.snuggles.commands.BaseCommand
import me.aroze.snuggles.commands.Feelings
import me.aroze.snuggles.commands.impl.generic.PingCommand
import me.aroze.snuggles.initialisation.Login.login
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.build.CommandData


lateinit var jda: JDA

fun main() {
    login()
    println(jda.selfUser.asTag)

    registerCommands(
        PingCommand
    )

}

private fun registerCommands(vararg commands: BaseCommand) {
    val queued: MutableList<CommandData> = ArrayList()
    for (command in commands) {
        jda.addEventListener(command)
        queued.add(command.build)
    }
    jda.updateCommands().addCommands(queued).queue()
}