package me.aroze.snuggles.util.type

import net.dv8tion.jda.api.EmbedBuilder

class FancyEmbed: EmbedBuilder() {

    init {
        setColor(0x2B2D31)
        setImage("attachment://bar.png")
    }

}