package me.aroze.snuggles.utils

import java.io.InputStream

fun getResourceStream(path: String) : InputStream? = Thread.currentThread().contextClassLoader.getResourceAsStream(path) ?: null
fun getBar() = getResourceStream("img/bar.png")!!