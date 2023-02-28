@file:JvmName("Snuggles")

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandHandler
import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.initialisation.Login.login
import me.aroze.snuggles.listeners.CountingListener
import me.aroze.snuggles.models.BotStats
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
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

    instance.addEventListener(CountingListener)

    println(instance.selfUser.asTag)

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

    Reflections("me.aroze.snuggles.commands.impl")
        .getTypesAnnotatedWith(Command::class.java)
        .filterIsInstance<Class<*>>()
        .map { CommandHandler.register(it) }

    instance.addEventListener(object : ListenerAdapter() {
        override fun onSlashCommand(event: SlashCommandEvent) {
            CommandHandler.execute(event)
        }
    })

    val lockCommandsToGuild = ConfigLoader.config.getBoolean("developers.lockCommandsToGuilds")
    val guilds = ConfigLoader.config.getList<String>("developers.guilds")

    val globalCommands = CommandHandler.commands.filter { !it.annotation.devOnly }
    val developmentCommands = CommandHandler.commands.filter { it.annotation.devOnly }

    if (lockCommandsToGuild) {
        instance.updateCommands().addCommands(globalCommands.map { it.build }).queue()
        for (id in guilds) {
            val guild = instance.getGuildById(id) ?: continue
            val commands = developmentCommands.map { it.build }
            guild.updateCommands().addCommands(commands).queue()
        }
    } else {
        instance.updateCommands().addCommands(CommandHandler.commands.map { it.build }).queue()
        for (id in guilds) {
            val guild = instance.getGuildById(id) ?: continue
            guild.retrieveCommands().queue { commands ->
                commands.forEach { command ->
                    guild.deleteCommandById(command.id).queue()
                }
            }
        }
    }

    Unit
}