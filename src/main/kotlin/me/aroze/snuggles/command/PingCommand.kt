package me.aroze.snuggles.command

import kotlinx.datetime.toKotlinInstant
import me.santio.coffee.common.annotations.Command
import me.santio.coffee.common.annotations.Optional
import me.santio.coffee.jda.annotations.Description
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.jetbrains.annotations.Nullable

@Command
@Description("Pongs")
class PingCommand {

    fun ping(
        event: SlashCommandInteractionEvent,
            @Optional @Description("Should this only show to you?") silent: Boolean?
    ) {

        val now = System.currentTimeMillis()
        val started = event.interaction.timeCreated.toInstant().toEpochMilli()

        event.reply("Pong! ${started - now}ms")
            .setEphemeral(silent ?: false)
            .queue()
    }

}