import me.aroze.snuggles.commands.BaseCommand
import me.aroze.snuggles.commands.feelings.Feelings
import me.aroze.snuggles.commands.feelings.RegisterFeelings.feelings
import me.aroze.snuggles.commands.impl.generic.PingCommand
import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.initialisation.Login.login
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import kotlin.collections.ArrayList

lateinit var jda: JDA

fun main() {

    ConfigLoader.load()

    login()
    println(jda.selfUser.asTag)

    registerCommands(
        PingCommand,
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