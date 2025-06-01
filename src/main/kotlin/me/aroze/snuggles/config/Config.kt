package me.aroze.snuggles.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val authentication: Authentication,
    val mongo: Mongo,
)

@Serializable
data class Authentication(
    val token: String,
)

@Serializable
data class Mongo(
    @SerialName("connection_string") val connectionString: String,
    @SerialName("database_name") val databaseName: String
)
