package me.aroze.snuggles.menu

import instance
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.utils.FancyEmbed
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import java.util.function.Consumer
import kotlin.random.Random

class ChannelMenu(
    private val topic: String,
    val description: String = "The menu below is *very self explanatory*. But if ur stupid ass needs help, I got u <3\n\n" +
        "The thingy that says `select a channel` is to select a channel. If the server has " +
        "a channel selected, the other thingy that says `toggle settings` becomes usable, " +
        "and allows you to, you guessed it, toggle settings.\n\n" +
        "...You're fucking welcome",
    val callback: Consumer<MenuUpdate>
): ListenerAdapter() {

    private val id = topic.lowercase().replace(" ", "-")
    private var textChannel: TextChannel? = null
    private val options: MutableList<MenuOption> = mutableListOf()
    private var selectedOptions = mutableListOf<String>()

    private val uid = Random.nextInt(1000000)
    private val channelId = "$id-channel-$uid"
    private val settingsId = "$id-settings-$uid"

    fun addOption(option: MenuOption): ChannelMenu {
        options.add(option)
        return this
    }

    fun setChannel(channel: TextChannel?): ChannelMenu {
        this.textChannel = channel
        return this
    }

    fun setSelectedOptions(options: List<String>?): ChannelMenu {
        if (options == null) return this
        this.selectedOptions.addAll(options.map { it.lowercase().replace(" ", "_") })
        return this
    }

    private fun makeChannelSelection(): EntitySelectMenu {
        val builder = EntitySelectMenu.create(channelId, EntitySelectMenu.SelectTarget.CHANNEL)
            .setChannelTypes(ChannelType.TEXT)
            .setPlaceholder("Select a channel for ${topic.lowercase()} to take place in.")
            .setRequiredRange(0, 1)

        if (textChannel != null) {
            builder.placeholder = "Select a channel. Current: #${textChannel?.name ?: "deleted channel"}"
        }

        return builder.build()
    }

    private fun makeSettingsSelection(): StringSelectMenu {
        val builder = StringSelectMenu.create(settingsId)
            .setPlaceholder("$topic settings (select a channel first!)")
            .setRequiredRange(0, 25)
            .setDisabled(textChannel == null)

        if (textChannel != null) builder.placeholder = "Toggle settings for the ${topic.lowercase()} module."

        for (option in options) {
            builder.addOption(
                option.name,
                option.id,
                option.description
            )
        }

        builder.setDefaultOptions(
            options.filter { selectedOptions.contains(it.id) }.map { it.toSelectOption() }
        )

        return builder.build()
    }

    private fun makeEmbed(): MessageEmbed {
        return FancyEmbed()
            .setTitle("$topic Settings <:flushed_cool:1081364373295087686>")
            .setDescription(description)
            .build()
    }

    fun send(message: SlashCommandInteractionEvent) {
        instance.addEventListener(this)

        message.replyEmbeds(makeEmbed())
            .addActionRow(makeChannelSelection())
            .addActionRow(makeSettingsSelection())
            .setEphemeral(true)
            .queue()
    }

    override fun onEntitySelectInteraction(event: EntitySelectInteractionEvent) = runBlocking {
        launch {
            if (event.componentId == channelId) {
                textChannel = event.mentions.channels.firstOrNull() as? TextChannel
                event.interaction.editMessageEmbeds(makeEmbed())
                    .setComponents(
                        ActionRow.of(makeChannelSelection()),
                        ActionRow.of(makeSettingsSelection())
                    )
                    .queue()

                callback.accept(MenuUpdate(
                    textChannel,
                    selectedOptions
                ))
            }
        }
        Unit
    }

    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) = runBlocking {
        launch {
            if (event.componentId == settingsId) {
                selectedOptions = event.values

                event.interaction.editMessageEmbeds(makeEmbed())
                    .setComponents(ActionRow.of(makeChannelSelection()), ActionRow.of(makeSettingsSelection()))
                    .queue()

                callback.accept(MenuUpdate(
                    textChannel,
                    selectedOptions
                ))
            }
        }
        Unit
    }

}