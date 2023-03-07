package me.aroze.snuggles.menu

import net.dv8tion.jda.api.interactions.components.selections.SelectOption

data class MenuOption(
    val name: String,
    val description: String,
    val default: Boolean = false
) {
    val id: String
        get() {
            return name.lowercase().replace(" ", "_")
        }

    fun toSelectOption(): SelectOption {
        return SelectOption.of(name, id)
    }
}
