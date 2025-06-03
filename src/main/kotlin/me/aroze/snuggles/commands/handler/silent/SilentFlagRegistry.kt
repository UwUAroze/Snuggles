package me.aroze.snuggles.commands.handler.silent

import net.dv8tion.jda.api.components.container.Container
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction

object SilentFlagRegistry {
    private val defaultSilentMap = mutableMapOf<String, Boolean>()

    /**
     * Registers a command with its silent flag.
     * This is used to set the default silent flag for commands.
     */
    fun register(commandName: String, defaultSilent: Boolean) {
        defaultSilentMap[commandName] = defaultSilent
    }

    /**
     * Retrieves the default silent flag for a command.
     */
    fun getDefault(commandName: String): Boolean? {
        return defaultSilentMap[commandName]
    }
}

/**
 * Extension property to check if the command interaction is marked to be silent. Returns null if no silent parameter is
 * provided and no default is registered.
 */
val GenericCommandInteractionEvent.silent: Boolean?
    get() {
        return this.getOption("silent")?.asBoolean
            ?: SilentFlagRegistry.getDefault(this.interaction.fullCommandName)
    }

/** Replies to the interaction event, respecting the silent flag. */
fun GenericCommandInteractionEvent.replySilently(message: String, overrideSilent: Boolean = true): ReplyCallbackAction {
    return this.reply(message)
        .setEphemeral(this.silent ?: overrideSilent)
}

/** Replies to the interaction event, respecting the silent flag. */
fun GenericCommandInteractionEvent.replyEmbedsSilently(
    vararg embeds: MessageEmbed,
    overrideSilent: Boolean = true
): ReplyCallbackAction {
    return this.replyEmbeds(embeds.toList())
        .setEphemeral(this.silent ?: overrideSilent)
}

fun GenericCommandInteractionEvent.replyComponentsSilently(
    components: Container,
    overrideSilent: Boolean = true
): ReplyCallbackAction {
    return this.replyComponents(components)
        .setEphemeral(this.silent ?: overrideSilent)
}
