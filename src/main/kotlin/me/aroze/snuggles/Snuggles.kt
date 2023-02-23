import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.BaseCommand
import me.aroze.snuggles.commands.feelings.RegisterFeelings.feelings
import me.aroze.snuggles.commands.feelings.RegisterFeelings.registerFeelings
import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.initialisation.Login.login
import me.aroze.snuggles.models.BotStats
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.reflections.Reflections
import java.util.Timer
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

lateinit var instance: JDA

var stats: BotStats = BotStats()

fun main() = runBlocking {

    ConfigLoader.load()
    Database.connect()

    login()
    println(instance.selfUser.asTag)

    registerFeelings()

    val commands = Reflections("me.aroze.snuggles.commands.impl")
        .getSubTypesOf(BaseCommand::class.java)
        .map { it.getDeclaredConstructor().newInstance() }

    registerCommands(
        *commands.toTypedArray(),
        *feelings.toTypedArray()
    )

    launch {
        Timer().schedule(300000) {
            Database.botStats.save()
        }
    }

    Runtime.getRuntime().addShutdownHook(Thread {
        Database.botStats.save()
        Database.disconnect()
        println("Database disconnected")
    })

    instance.getGuildById(892579969329414155)?.let { devGuild ->
        registerDevCommands(devGuild,
            *commands.filter { it.devOnly }.toTypedArray()
        )
    }

    Unit
}

private fun registerCommands(vararg commands: BaseCommand) {
    val queued: MutableList<CommandData> = ArrayList()
    for (command in commands) {
        instance.addEventListener(command)
        queued.add(command.build)
    }
    instance.updateCommands().addCommands(queued).queue()
}