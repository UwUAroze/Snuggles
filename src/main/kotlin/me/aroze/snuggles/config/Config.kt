package me.aroze.snuggles.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val authentication: Authentication,
    val bot: Bot,
    val developers: Developers
)

@Serializable
data class Authentication(
    val token: String,
    val mongo: String
)

@Serializable
data class Bot(
    @SerialName("invite_link")
    val inviteLink: String,
)

@Serializable
data class Developers(
    val owner: Long,
    val developers: List<Long>
)