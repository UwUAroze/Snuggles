package me.aroze.snuggles.util

import me.aroze.snuggles.util.type.FancyEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

object Responses {

    fun SlashCommandInteractionEvent.errorGuildOnly() {
        val eb = FancyEmbed()
            .addField("Awwwwhh.",
                """
                This command is only supported in guilds ;c. If you really want support for it in dms, well you're weird, but maybe shout at a dev!
                ~~Then again, we probably won't care.~~ Love you <3
                """.trimIndent(), false)

        this.replyEmbeds(eb.build())
            .bar(BarStyle.ERROR)
            .setEphemeral(true)
            .queue()

    }

}