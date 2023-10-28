package me.aroze.snuggles.setup

import me.aroze.snuggles.database.models.features.generic.GenericFeature
import me.aroze.snuggles.util.type.FancyEmbed
import me.santio.coffee.jda.gui.button.Button
import me.santio.coffee.jda.gui.dropdown.Dropdown
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction
import java.time.Duration

class ChannelSetup<T: GenericFeature>(
    private val feature: String,
    private val clazz: Class<T>,
    private val title: String = "$feature setup <:flushed_cool:1081364373295087686>"
) {

    private var enabled = false
    private var updateCallback: (T?, Boolean) -> Unit = { _, _ ->}
    private var channelChangedCallback: (String?) -> Unit = {}

    /**
     * Sets whether this flow is already enabled or not
     * @param enabled Whether the flow is enabled
     * @return This channel setup instance
     */
    fun isEnabled(enabled: Boolean): ChannelSetup<T> {
        this.enabled = enabled
        return this
    }

    /**
     * Add a custom handler for when the options have been changed, the second value is
     * whether the setup is enabled or not
     * **Notice**, this removes any non option fields back to their defaults
     * @return This channel setup instance
     */
    fun onUpdate(callback: (T?, Boolean) -> Unit): ChannelSetup<T> {
        this.updateCallback = callback
        return this
    }

    /**
     * Add a custom handler for when the channel selected has changed
     * @return This channel setup instance
     */
    fun onChannelChanged(callback: (String?) -> Unit): ChannelSetup<T> {
        this.channelChangedCallback = callback
        return this
    }

    /**
     * Builds the dropdown, this builds a String Select Menu
     */
     private fun createDropdown(): Dropdown<*> {
        return Dropdown.from(clazz) {
            updateCallback(it.selected, enabled)
        }.multiple()
    }

    /**
     * Creates a channel select menu
     */
    private fun createChannelDropdown(): Dropdown<*> {
        return Dropdown.from(EntitySelectMenu.SelectTarget.CHANNEL) {
            channelChangedCallback(it.selected.channels.firstOrNull()?.id)
        }.multiple()
    }

    /**
     * Creates the button required for either enabling or disabling the feature
     */
    private fun createToggleButton(): Button {
        return Button.create(
            (if (enabled) "Disable " else "Enable ") + feature,
            if (enabled) ButtonStyle.DANGER else ButtonStyle.SUCCESS,
            expiry = Duration.ofDays(7)
        ) { button ->
            enabled = !enabled
            updateCallback(null, enabled)

            val message = send(button.event)
            button.event.editMessageEmbeds(message.embeds)
                .setComponents(message.components)
                .queue()
        }
    }

    /**
     * Gets a message setup for being queued to discord
     * @param message The slash command interaction event
     * @return The reply callback action
     */
    fun send(message: IReplyCallback): ReplyCallbackAction {
        if (!enabled) {
            return message.replyEmbeds(
                FancyEmbed()
                    .setTitle(title)
                    .setDescription("Enable this option to configure settings.")
                    .build()
            ).setEphemeral(true)
             .addActionRow(createToggleButton().build())
        } else {
            return message.replyEmbeds(
                FancyEmbed()
                    .setTitle(title)
                    .setDescription("Select the options you want to change.")
                    .build()
            )
                .setEphemeral(true)
                .addActionRow(createChannelDropdown().build())
                .addActionRow(createDropdown().build())
                .addActionRow(createToggleButton().build())
        }
    }

}