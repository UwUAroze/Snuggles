package me.aroze.snuggles.util.type

import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.selections.SelectOption

data class Option(
    val name: String,
    val description: String,
    var enabled: Boolean = false,
    var emoji: Emoji? = null,
    val callback: (Boolean) -> Unit
) {

    val id = name.lowercase().replace(" ", "_")

    fun toSelectOption() = SelectOption.of(name, id)
        .withLabel(name)
        .withDescription(description)
        .withEmoji(emoji)

}