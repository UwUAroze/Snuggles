package me.aroze.snuggles.commands.impl.generic

import instance
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.commands.BaseCommand
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar
import me.aroze.snuggles.utils.getBar
import me.aroze.snuggles.utils.toMember
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.utils.TimeFormat
import java.io.File
import java.time.format.DateTimeFormatter

class UserInfoCommand : BaseCommand("userinfo", "Displays useful information about a user or server member") {

    init {
        options.add(Option(
            OptionType.USER,
            "target",
            "User or member to lookup information on",
            true
        ))
    }

    override fun onExecute(event: SlashCommandEvent) {

        val user = event.getOption("target")?.asUser ?: return

        val eb = FancyEmbed()
            .setTitle(user.asTag)
            .setDescription("hmmm")
            .addField("Origin", "<t:${user.timeCreated.toEpochSecond()}>" , false)
            .setThumbnail(user.effectiveAvatarUrl)

        user.toMember(event.guild!!) { member ->

            if (member != null) {
                eb.addField("Joined", "<t:${member.timeJoined.toEpochSecond()}>" , false)
                if (member.timeBoosted != null) eb.addField("Booster", "Boosting since <t:${member.timeBoosted!!.toEpochSecond()}:R>" , false)
            }

            event.replyEmbeds(eb.build())
                .bar()
                .setEphemeral(silent)
                .queue()

        }
    }

}