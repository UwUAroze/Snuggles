import me.aroze.snuggles.commands.BaseCommand
import me.aroze.snuggles.commands.feelings.RegisterFeelings.feelings
import me.aroze.snuggles.commands.feelings.RegisterFeelings.registerFeelings
import me.aroze.snuggles.commands.impl.generic.GlobalStatsCommand
import me.aroze.snuggles.commands.impl.generic.PingCommand
import me.aroze.snuggles.commands.impl.generic.UwUifyCommand
import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.initialisation.Login.login
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import kotlin.collections.ArrayList

lateinit var jda: JDA

fun main() {

    ConfigLoader.load()

    login()
    println(jda.selfUser.asTag)

    registerFeelings()

    registerCommands(
        GlobalStatsCommand,
        PingCommand,
        UwUifyCommand,
        *feelings.toTypedArray()
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