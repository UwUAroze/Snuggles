package me.aroze.snuggles.commands.impl.generic

import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import net.dv8tion.jda.api.entities.User

@Command(
    name = "ship",
    description = "Ships two people together"
)
class ShipNameCommand {

    fun users(event: CommandEvent, user1: User, user2: User) {
        val names = getShipName(user1.name, user2.name)
        val shippedName = names.random()
        event.message.reply("${user1.name} x ${user2.name} = $shippedName").setEphemeral(event.silent).queue()
    }

    fun names(event: CommandEvent, name1: String, name2: String) {
        val shippedName = getShipName(name1, name2).random()
        event.message.reply("${name1} x ${name2} = $shippedName").setEphemeral(event.silent).queue()
    }

    private fun getShipName(name1: String, name2: String): List<String> {
        val name1Length = name1.length
        val name2Length = name2.length
        val name1Half = name1Length / 2
        val name2Half = name2Length / 2
        val name1HalfRemainder = name1Length % 2
        val name2HalfRemainder = name2Length % 2
        val name1Half1 = name1.substring(0, name1Half + name1HalfRemainder)
        val name1Half2 = name1.substring(name1Half + name1HalfRemainder)
        val name2Half1 = name2.substring(0, name2Half + name2HalfRemainder)
        val name2Half2 = name2.substring(name2Half + name2HalfRemainder)
        return listOf(
            name1Half1 + name2Half2,
            name2Half1 + name1Half2,
            name1Half1 + name2Half1,
            name1Half2 + name2Half2,
            name1Half1 + name2Half2 + name1Half2,
            name1Half1 + name2Half1 + name2Half2,
            name2Half1 + name1Half1 + name1Half2,
            name2Half1 + name1Half2 + name2Half2,
        ).map { it.lowercase() }
    }

}