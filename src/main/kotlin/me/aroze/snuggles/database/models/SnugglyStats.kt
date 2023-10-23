package me.aroze.snuggles.database.models

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.database.Database.database
import org.bson.codecs.pojo.annotations.BsonIgnore

data class SnugglyStats(
    var totalExecutions: Int = 0,
    var totalCounts: Int = 0,
    var totalTrackedMessages: Int = 0,
    var totalLoggedMessages: Int = 0
) {

    @BsonIgnore
    fun save() {
        val collection = database.getCollection<SnugglyStats>("SnugglyStats")
        collection.findOneAndReplace(
            Filters.gt("totalExecutions", -1),
            this,
            FindOneAndReplaceOptions().upsert(true)
        )
    }

}