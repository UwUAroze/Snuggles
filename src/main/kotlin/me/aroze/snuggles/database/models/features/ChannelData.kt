package me.aroze.snuggles.database.models.features

import com.mongodb.client.model.Filters
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.database.models.SnugglyStats
import me.aroze.snuggles.database.models.features.generic.impl.LoggingData
import me.aroze.snuggles.initialisation.BotLoader.snuggles
import org.bson.BsonUndefined

data class ChannelData(
    val channel: String,
    val logging: LoggingData? = null,
) {

    companion object {
        fun getByGuild(id: String): List<ChannelData> {
            val collection = Database.database.getCollection<ChannelData>("ChannelData")
            val channels = snuggles.getGuildById(id)?.textChannels?.map { it.id } ?: listOf()
            return collection.find(Filters.`in`("channel", channels)).toList()
        }
    }

}