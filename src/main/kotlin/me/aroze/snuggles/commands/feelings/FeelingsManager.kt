package me.aroze.snuggles.commands.feelings

import instance
import me.aroze.snuggles.commands.handler.CommandEvent
import java.util.function.Function

object FeelingsManager {

    private val currentIndex = mutableMapOf<Feelings, MutableMap<String, MutableMap<FeelingList, MutableList<Int>>>>()

    fun getFeeling(guild: String, feelings: Feelings, list: FeelingList): String {
        val feelingsMap = currentIndex.getOrPut(feelings) { mutableMapOf() }
        val guildMap = feelingsMap.getOrPut(guild) { mutableMapOf() }
        val indexes = guildMap.getOrPut(list) { mutableListOf() }

        if (indexes.isEmpty()) {
            for (i in 0 until list.getList.apply(feelings).size) {
                indexes.add(i)
            }
            indexes.shuffle()
        }

        val index = indexes.removeAt(0)
        return list.getList.apply(feelings)[index]
    }

    fun getFeelingMessage(guild: String, feeling: Feelings, event: CommandEvent): String {
        val user = event.message.user

        return when (event.message.options.find { it.name == "target" }!!.asUser) {
            user -> getFeeling(guild, feeling, FeelingList.SELF)
            instance.selfUser -> getFeeling(guild, feeling, FeelingList.BOT)
            else -> getFeeling(guild, feeling, FeelingList.USER)
        }
    }

    fun handleFeeling(feeling: Feelings, event: CommandEvent) {
        val guild = event.message.guild?.id ?: return

        val message: String = getFeelingMessage(guild, feeling, event)
        val user = event.message.options.find { it.name == "target" }!!.asUser

        event.message.reply(
            message.replace("{user}", event.message.user.asMention)
                .replace("{target}", user.asMention)
        ).setEphemeral(event.silent).queue()
    }

    enum class FeelingList(val getList: Function<Feelings, List<String>>) {
        USER({ it.messages }),
        SELF({ it.self }),
        BOT({ it.bot }),
    }

}