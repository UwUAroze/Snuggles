package me.aroze.snuggles.util

import net.dv8tion.jda.api.JDA

fun JDA.getAllUsers() : Int {
    var count = 0
    this.guilds.forEach { count += it.memberCount }
    return count
}