package me.aroze.snuggles.database.models.features

import com.mongodb.client.model.Filters
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.database.models.features.generic.impl.LoggingData
import me.aroze.snuggles.initialisation.BotLoader.snuggles

data class ChannelData(
    val channel: String,
    val logging: LoggingData? = null,
) {

    companion object {
        fun getByGuild(guild: String): List<ChannelData> {
            val collection = Database.database.getCollection<ChannelData>("ChannelData")
            val channels = snuggles.getGuildById(guild)?.textChannels?.map { it.id } ?: listOf()
            return collection.find(Filters.`in`("channel", channels)).toList()
        }

        fun getLoggingData(guild: String) = getSpecificData(guild, "logging")?.logging

        private fun getSpecificData(guild: String, type: String): ChannelData? {
            val collection = Database.database.getCollection<ChannelData>("ChannelData")
            val channels = snuggles.getGuildById(guild)?.textChannels?.map { it.id } ?: listOf()
            return collection.find(Filters.and(
                Filters.`in`("channel", channels),
                Filters.exists(type)
            )).toList().firstOrNull()
        }

    }

}