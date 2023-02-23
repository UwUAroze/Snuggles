package me.aroze.snuggles.models

import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.database.database
import org.bson.codecs.pojo.annotations.BsonIgnore
import org.litote.kmongo.getCollection
import org.litote.kmongo.gt

data class BotStats(
    var totalExecutions: Int = 0
) {

    @BsonIgnore
    fun save() {
        val collection = database.getCollection<BotStats>()
        collection.findOneAndReplace(
            ::totalExecutions gt -1,
            this,
            FindOneAndReplaceOptions().upsert(true)
        )
    }

}
