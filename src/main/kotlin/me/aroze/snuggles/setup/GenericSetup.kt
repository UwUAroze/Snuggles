//package me.aroze.snuggles.setup
//
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//import me.aroze.arozeutils.kotlin.extension.addAll
//import me.aroze.snuggles.initialisation.BotLoader.snuggles
//import me.aroze.snuggles.util.type.FancyEmbed
//import me.aroze.snuggles.util.type.Option
//import me.santio.coffee.jda.gui.addActionRow
//import me.santio.coffee.jda.gui.button.Button
//import net.dv8tion.jda.api.entities.MessageEmbed
//import net.dv8tion.jda.api.entities.channel.ChannelType
//import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
//import net.dv8tion.jda.api.entities.emoji.Emoji
//import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
//import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
//import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent
//import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
//import net.dv8tion.jda.api.hooks.ListenerAdapter
//import net.dv8tion.jda.api.interactions.components.ActionRow
//import net.dv8tion.jda.api.interactions.components.LayoutComponent
//import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
//import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu
//import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
//import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction
//import java.util.*
//
//class GenericSetup(
//    private val feature: String,
//    private var enabled: Boolean,
//    private val title: String = "$feature setup <:flushed_cool:1081364373295087686>",
//): ListenerAdapter() {
//
//    private var textChannel: String? = null
//    private val options = mutableListOf<Option>()
//    private val id = UUID.randomUUID().toString().substring(0, 12)
//    private var channelCallback: ((String?, Boolean) -> Unit)? = null
//
//    fun addOption(name: String, description: String, enabled: Boolean = false, emoji: Emoji? = null, callback: (Boolean) -> Unit): GenericSetup {
//        options.add(Option(name, description, enabled, emoji, callback))
//        return this
//    }
//
//    fun onFinish(callback: (String?, Boolean) -> Unit): GenericSetup {
//        channelCallback = callback
//        return this
//    }
//
//    fun setChannel(channel: String?): GenericSetup {
//        textChannel = channel
//        return this
//    }
//
//    private fun build(): MessageEmbed {
//        return FancyEmbed()
//            .setTitle(title)
//            .setDescription("Select the options you want to change.")
//            .build()
//    }
//
//    private fun createChannelSelection(): EntitySelectMenu {
//        val builder = EntitySelectMenu.create("setup:channel:$id", EntitySelectMenu.SelectTarget.CHANNEL)
//            .setChannelTypes(ChannelType.TEXT)
//            .setPlaceholder("Select a channel for ${feature.lowercase()} to take place in.")
//            .setRequiredRange(0, 1)
//
//        val channel = textChannel?.let { snuggles.getTextChannelById(it) }
//        if (channel != null) builder.setDefaultValues(EntitySelectMenu.DefaultValue.channel(channel.id))
//
//        return builder.build()
//    }
//
//    private fun createOptionsMenu(): StringSelectMenu {
//        return StringSelectMenu.create("setup:settings:$id")
//            .setRequiredRange(0, options.size)
//            .setDisabled(textChannel == null)
//            .setPlaceholder("Select an option")
//            .addOptions(options.map { it.toSelectOption() })
//            .setDefaultOptions(options.filter { it.enabled }.map { it.toSelectOption() })
//            .build()
//    }
//
//    private fun createToggleButton(): Button {
//        return Button.create(
//            (if (enabled) "Disable " else "Enable ") + feature,
//            if (enabled) ButtonStyle.DANGER else ButtonStyle.SUCCESS,
//        ) {
//            enabled = !enabled
//            it.event.editComponents(createActionRows()).queue()
//        }
//
//    }
//
//    private fun createActionRows(): List<LayoutComponent> {
//        val actionRows = mutableListOf<ActionRow>()
//        if (enabled) actionRows.addAll(
//            ActionRow.of(createChannelSelection()),
//            ActionRow.of(createOptionsMenu())
//        )
//        actionRows.add(ActionRow.of(createToggleButton().build()))
//        return actionRows
//    }
//
//    fun send(message: SlashCommandInteractionEvent): ReplyCallbackAction {
//        snuggles.addEventListener(this)
//
//        return message.replyEmbeds(build())
//            .addComponents(createActionRows())
//            .setEphemeral(true)
//    }
//
//    override fun onEntitySelectInteraction(event: EntitySelectInteractionEvent) = runBlocking {
//        launch { handleInteraction(event) }
//        Unit
//    }
//
//    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) = runBlocking {
//        launch { handleInteraction(event) }
//        Unit
//    }
//
//    private fun handleInteraction(event: GenericSelectMenuInteractionEvent<*, *>) {
//
//        if (!event.componentId.startsWith("setup:")) return
//
//        if (event is EntitySelectInteractionEvent) textChannel = (event.mentions.channels.firstOrNull() as TextChannel?)?.id
//        else if (event is StringSelectInteractionEvent) {
//            options.forEach {
//                val previous = it.enabled
//                it.enabled = event.values.contains(it.id)
//                if (it.enabled != previous) it.callback(it.enabled)
//            }
//        }
//
//        channelCallback?.invoke(textChannel, enabled)
//
//        event.interaction.editMessageEmbeds(build())
//            .setComponents(createActionRows()).queue()
//    }
//
//}