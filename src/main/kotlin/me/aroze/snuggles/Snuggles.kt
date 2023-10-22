package me.aroze.snuggles

import me.aroze.snuggles.command.PingCommand
import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.initialisation.BotLoader
import me.aroze.snuggles.initialisation.BotLoader.snuggles
import me.santio.coffee.common.Coffee
import me.santio.coffee.common.registry.AdapterRegistry
import me.santio.coffee.jda.CoffeeJDA

fun main(args: Array<String>) {

    println(toBoxed(Boolean::class.java))
    println(toBoxed(Boolean::class.javaPrimitiveType!!))

    ConfigLoader.load()
    BotLoader.initialise()

    println("Successfully logged into: ${snuggles.selfUser.asTag}")

}

private fun toBoxed(primitive: Class<*>): Class<*> {
    if (!primitive.isPrimitive) return primitive
    return Class.forName("java.lang.${primitive.name.replaceFirstChar { it.uppercase() }}")
}