package me.aroze.snuggles.commands.handler.processor

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.aroze.snuggles.database.entity.CommandLog
import me.aroze.snuggles.database.service.impl.CommandLogService
import org.incendo.cloud.discord.jda5.JDAInteraction
import org.incendo.cloud.execution.postprocessor.CommandPostprocessingContext
import org.incendo.cloud.execution.postprocessor.CommandPostprocessor

class CommandPostProcessTasks : CommandPostprocessor<JDAInteraction> {

    override fun accept(context: CommandPostprocessingContext<JDAInteraction?>) {
        val interaction = context.commandContext().sender()
        val event = interaction.interactionEvent()
            ?: return

        runBlocking {
            CommandLogService.logCommand(
                CommandLog(
                    executorId = event.user.idLong,
                    command = event.fullCommandName,
                    arguments = collectOptions(interaction),
                    channelId = event.channelIdLong,
                    timestamp = Instant.fromEpochSeconds(event.timeCreated.toEpochSecond())
                )
            )
        }
    }

    fun collectOptions(interaction: JDAInteraction): Map<String, String> {
        val optionValues = mutableMapOf<String, String>()
        interaction.optionMappings().forEach { optionMapping ->
            optionValues[optionMapping.name] = optionMapping.asString
        }
        return optionValues

    }

}
