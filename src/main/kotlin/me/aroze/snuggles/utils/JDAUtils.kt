package me.aroze.snuggles.utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction

fun User.toMember(guild: Guild, callback: (Member?) -> Unit) {
    guild.retrieveMember(this).queue({
        callback.invoke(it)
    }, {
        callback.invoke(null)
    })
}

fun ReplyAction.bar(type: BarStyle): ReplyAction {
    this.addFile(getResourceStream(type.img)!!, "bar.png")
    return this
}

class FancyEmbed: EmbedBuilder() {
    init {
        setColor(0x2F3136)
        setImage("attachment://bar.png")
    }
}

enum class BarStyle(val img: String) {
    PINK("img/bar_pink.png"),
    ERROR("img/bar_error.png")
}