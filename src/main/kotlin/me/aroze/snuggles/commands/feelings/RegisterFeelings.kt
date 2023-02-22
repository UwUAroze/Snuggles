package me.aroze.snuggles.commands.feelings

import instance
import me.aroze.snuggles.commands.BaseCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType

object RegisterFeelings {

    val feelings = mutableListOf<BaseCommand>()
    val feelingsIndexes = mutableMapOf<Feelings, MutableMap<Int, Int>>()

    fun registerFeelings() {

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

                    /* Welcome to my messy and over-engineered randomisation!!
                    The idea is that each list will be shuffled, and gone through in order, then shuffled again once finished
                    This way, there'll be like 50 unique messages in a row (relative to each other, and provided there is enough messages)
                    ^ Works much better than randomly picking a message from the list; duplicates are much more noticeable that way */

                    if (target.id == instance.selfUser.id && it.bot.isNotEmpty()) { messages = it.bot; subIndex = 0 }
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

                    event.reply(message).setEphemeral(silent).queue()
                }
            })
        }
    }

}