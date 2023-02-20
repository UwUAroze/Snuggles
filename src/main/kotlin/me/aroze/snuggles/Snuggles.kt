import me.aroze.snuggles.commands.BaseCommand
import me.aroze.snuggles.commands.Feelings
import me.aroze.snuggles.commands.impl.generic.PingCommand
import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.initialisation.Login.login
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import java.util.*
import kotlin.collections.ArrayList

lateinit var jda: JDA

fun main() {

    ConfigLoader.load()

    login()
    println(jda.selfUser.asTag)

    val feelings = mutableListOf<BaseCommand>()
    val feelingsIndexes = mutableMapOf<Feelings, MutableMap<Int, Int>>()

    // Register feelings
    Feelings.values().forEach {

        it.messages = it.messages.shuffled()
        it.self = it.self.shuffled()
        it.bot = it.bot.shuffled()

        feelingsIndexes[it] = mutableMapOf(0 to 0, 1 to 0, 2 to 0)

        feelings.add(object : BaseCommand(it.feeling.lowercase(), it.description) {
            init {
                options.add(Option(
                    OptionType.USER,
                    "target",
                    "Choose a user to ${it.feeling.lowercase()}",
                    true
                ))
            }

            override fun onExecute(event: SlashCommandEvent) {
                val target = event.getOption("target")?.asUser ?: return

                var subIndex: Int
                val messages: List<String>

                if (target.id == jda.selfUser.id && it.bot.isNotEmpty()) { messages = it.bot; subIndex = 0 }
                else if (event.user.id == target.id && it.self.isNotEmpty()) { messages = it.self; subIndex = 1 }
                else messages = it.messages; subIndex = 2

                val currentIndex = feelingsIndexes[it]?.get(subIndex) ?: 0
                if (currentIndex >= messages.size - 1) {
                    it.messages = it.messages.shuffled()
                    feelingsIndexes[it]?.set(subIndex, 0)
                } else feelingsIndexes[it]?.set(subIndex, currentIndex + 1)

                val message = messages[feelingsIndexes[it]?.get(subIndex)!!]
                    .replace("{user}", event.user.asMention)
                    .replace("{target}", target.asMention)

                event.reply(message).queue()
            }
        })
    }

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