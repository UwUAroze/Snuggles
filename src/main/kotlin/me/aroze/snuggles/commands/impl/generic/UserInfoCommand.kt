package me.aroze.snuggles.commands.impl.generic

import me.aroze.snuggles.commands.handler.Command
import me.aroze.snuggles.commands.handler.CommandEvent
import me.aroze.snuggles.utils.BarStyle
import me.aroze.snuggles.utils.FancyEmbed
import me.aroze.snuggles.utils.bar
import me.aroze.snuggles.utils.toMember
import net.dv8tion.jda.api.entities.User

@Command(
    description = "Displays useful information about a user or server member"
)
class UserInfoCommand {
    fun main(event: CommandEvent, target: User) {
        val eb = FancyEmbed()
            .setTitle(target.asTag)
            .setDescription("hmmm")
            .addField("Origin", "<t:${target.timeCreated.toEpochSecond()}>" , false)
            .setThumbnail(target.effectiveAvatarUrl)

        target.toMember(event.message.guild!!) { member ->

            if (member != null) {
                eb.addField("Joined", "<t:${member.timeJoined.toEpochSecond()}>" , false)
                if (member.timeBoosted != null) eb.addField("Booster", "Boosting since <t:${member.timeBoosted!!.toEpochSecond()}:R>" , false)
            }

            event.message.replyEmbeds(eb.build())
                .bar(BarStyle.PINK)
                .setEphemeral(event.silent)
                .queue()
        }
    }

}