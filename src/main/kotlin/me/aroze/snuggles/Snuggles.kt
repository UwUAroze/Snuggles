package me.aroze.snuggles

import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.initialisation.BotLoader
import me.aroze.snuggles.initialisation.BotLoader.snuggles

fun main(args: Array<String>) {
    ConfigLoader.load()
    BotLoader.login()
    snuggles.awaitReady()

    println("Successfully logged into: ${snuggles.selfUser.asTag}")
}