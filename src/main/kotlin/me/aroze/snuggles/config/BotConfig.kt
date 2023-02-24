package me.aroze.snuggles.config

data class BotConfig(

    val token: String,
    val mongo: String,
    val developers: List<String>,

)