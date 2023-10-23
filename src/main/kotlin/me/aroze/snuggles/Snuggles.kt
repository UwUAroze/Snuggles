package me.aroze.snuggles

import me.aroze.snuggles.command.PingCommand
import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.initialisation.BotLoader
import me.aroze.snuggles.initialisation.BotLoader.snuggles
import me.santio.coffee.common.Coffee
import me.santio.coffee.common.registry.AdapterRegistry
import me.santio.coffee.jda.CoffeeJDA
import java.util.Timer
import kotlin.concurrent.schedule

fun main(args: Array<String>) {

    ConfigLoader.load()
    Database.connect()
    BotLoader.initialise()

    println("Successfully logged into: ${snuggles.selfUser.asTag}")

}