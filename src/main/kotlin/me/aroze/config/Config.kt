package me.aroze.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val authentication: Authentication
)

@Serializable
data class Authentication(
    val token: String
)