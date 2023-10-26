package me.aroze.snuggles.command

import me.aroze.snuggles.database.models.features.ChannelData
import me.aroze.snuggles.database.models.features.ChannelData.Companion.getLoggingData
import me.aroze.snuggles.database.models.features.generic.impl.LoggingData
import me.aroze.snuggles.setup.GenericSetup
import me.aroze.snuggles.util.Responses.errorGuildOnly
import me.santio.coffee.common.annotations.Command
import me.santio.coffee.jda.annotations.Description
import me.santio.coffee.jda.annotations.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

import net.dv8tion.jda.api.Permission as JDAPermission

@Command
@Permission(JDAPermission.MANAGE_CHANNEL)
@Description("Setup different features of Snuggles.")
class SetupCommand {

    @Description("Setup or change settings relating to logging")
    fun logging(event: SlashCommandInteractionEvent) {

        if (event.guild == null) return event.errorGuildOnly()

        val logging = getLoggingData(event.guild!!.id) ?: LoggingData()
        var channelData = ChannelData.getByGuild(event.guild!!.id).firstOrNull { it.logging != null }

        GenericSetup("Logging", logging.enabled)
            .setChannel(channelData?.channel)
            .addOption("Log message changes", "Sends a log whenever a message is edited or deleted.", logging.logMessageChanges) { logging.logMessageChanges = it }
            .onFinish { id, enabled -> channelData = updateChannelData(id, enabled, channelData, logging = logging) }
            .send(event)
            .queue()

    }

    @Description("Setup or change settings relating to counting")
    fun counting(event: SlashCommandInteractionEvent) {
        event.reply("b").queue()
    }

    private fun updateChannelData(id: String?, enabled: Boolean, channelData: ChannelData?, logging: LoggingData? = null): ChannelData? {
        var data = channelData
        logging?.enabled = enabled

        if (id != data?.channel) {
            data?.delete()
            data = id?.let { ChannelData(it, logging) }
        }

//        data?.save()
        return data
    }

}