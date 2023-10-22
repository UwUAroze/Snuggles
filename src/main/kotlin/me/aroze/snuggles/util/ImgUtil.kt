package me.aroze.snuggles.util

import net.dv8tion.jda.api.requests.restaction.MessageCreateAction
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction
import net.dv8tion.jda.api.utils.FileUpload
import java.io.InputStream

enum class BarStyle(val img: String) {
    PINK("img/bar_pink.png"),
    ERROR("img/bar_error.png")
}

fun ReplyCallbackAction.bar(type: BarStyle) =
    this.addFiles(FileUpload.fromData(getResourceStream(type.img)!!, "bar.png"))

fun MessageCreateAction.bar(type: BarStyle) =
    this.addFiles(FileUpload.fromData(getResourceStream(type.img)!!, "bar.png"))

fun getResourceStream(path: String) : InputStream? =
    Thread.currentThread().contextClassLoader.getResourceAsStream(path) ?: null