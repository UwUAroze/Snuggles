package me.aroze.snuggles.database.models.features

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.database.models.SnugglyStats
import me.aroze.snuggles.database.models.features.generic.impl.LoggingData
import me.aroze.snuggles.initialisation.BotLoader.snuggles
import org.bson.codecs.pojo.annotations.BsonIgnore

data class ChannelData(
    val channel: String,
    val logging: LoggingData? = null,
) {

    fun save() {
        val collection = Database.database.getCollection<ChannelData>("ChannelData")
        collection.findOneAndReplace(
            Filters.eq("channel", channel),
            this,
            FindOneAndReplaceOptions().upsert(true)
        )
    }

    fun delete() {
        val collection = Database.database.getCollection<ChannelData>("ChannelData")
        collection.deleteOne(Filters.eq("channel", channel))
    }

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