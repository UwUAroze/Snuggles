import me.aroze.snuggles.commands.BaseCommand
import me.aroze.snuggles.commands.Feelings
import me.aroze.snuggles.commands.impl.generic.PingCommand
import me.aroze.snuggles.initialisation.Login.login
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import java.util.*
import kotlin.collections.ArrayList

lateinit var jda: JDA

fun main() {
    login()
    println(jda.selfUser.asTag)

    val feelings = mutableListOf<BaseCommand>()

    // Register feelings
    Feelings.values().forEach {
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

                var messages: List<String> = listOf()
                if (target.isBot && it.bot.isNotEmpty()) messages = it.bot
                else if (event.user.id == target.id && it.self.isNotEmpty()) messages = it.self
                else messages = it.messages

                val random = Random()
                val message = messages[random.nextInt(messages.size)]
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