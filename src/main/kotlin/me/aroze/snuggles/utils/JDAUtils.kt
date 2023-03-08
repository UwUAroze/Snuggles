package me.aroze.snuggles.utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction
import net.dv8tion.jda.api.utils.FileUpload

fun User.toMember(guild: Guild, callback: (Member?) -> Unit) {
    guild.retrieveMember(this).queue({
        callback.invoke(it)
    }, {
        callback.invoke(null)
    })
}

fun ReplyCallbackAction.bar(type: BarStyle): ReplyCallbackAction {
    return this.addFiles(FileUpload.fromData(getResourceStream(type.img)!!, "bar.png"))
}

fun MessageCreateAction.bar(type: BarStyle): MessageCreateAction {
    return this.addFiles(FileUpload.fromData(getResourceStream(type.img)!!, "bar.png"))
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

fun JDA.getAllUsers() : Int {
    var count = 0
    this.guilds.forEach { count += it.memberCount }
    return count
}