@file:JvmName("Snuggles")

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.BaseCommand
import me.aroze.snuggles.commands.DevCommand
import me.aroze.snuggles.commands.feelings.RegisterFeelings.feelings
import me.aroze.snuggles.commands.feelings.RegisterFeelings.registerFeelings
import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.initialisation.Login.login
import me.aroze.snuggles.models.BotStats
import me.aroze.snuggles.utils.safeConstruct
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import org.reflections.Reflections
import java.util.*
import kotlin.concurrent.schedule

lateinit var instance: JDA

var stats: BotStats = BotStats()

fun main() = runBlocking {

    ConfigLoader.load()
    Database.connect()

    login()
    instance.awaitReady()
    println(instance.selfUser.asTag)

    registerFeelings()

    val commands = Reflections("me.aroze.snuggles.commands.impl")
        .getSubTypesOf(BaseCommand::class.java)
        .mapNotNull { it.safeConstruct() }

    val cmds = registerCommands(
        *commands.filter {!it.devOnly}.toTypedArray(),
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

    registerDevCommands(cmds, *commands.filter { it.devOnly }.toTypedArray())

    Unit
}

private fun registerCommands(vararg commands: BaseCommand): List<CommandData> {
    val queued: MutableList<CommandData> = ArrayList()
    for (command in commands) {
        instance.addEventListener(command)
        queued.add(command.build)
    }
    instance.updateCommands().addCommands(queued).queue()
    return queued
}

private fun registerDevCommands(original: List<CommandData>, vararg commands: BaseCommand) {
    val queued: MutableList<CommandData> = ArrayList()

    for (command in commands) {
        instance.addEventListener(command)
        if (command !is DevCommand) queued.add(command.build)
        else {

            val commandName = command.info[0]
            val existing = queued.find { it.name == commandName }
            if (existing == null) queued.add(CommandData(commandName, "Development Command"))

            queued.find { it.name == commandName }?.addSubcommands(SubcommandData(
                command.info[1],
                command.description
            ).addOptions(*command.optionData.toTypedArray()))
        }
    }

    if (ConfigLoader.config.getBoolean("developers.lockCommandsToGuilds")) {
        for (guild in ConfigLoader.config.getList<String>("developers.guilds")) {
            for (command in queued) instance.getGuildById(guild)?.upsertCommand(command)?.queue()
        }
    } else for (command in queued) instance.upsertCommand(command).queue()

}