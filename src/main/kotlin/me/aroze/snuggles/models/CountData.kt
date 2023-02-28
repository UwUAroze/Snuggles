package me.aroze.snuggles.models

import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.database.database
import org.bson.codecs.pojo.annotations.BsonIgnore
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection

data class CountData(
    val id: String,
    var count: Int = 0
) {

    @BsonIgnore
    fun save() {
        val collection = database.getCollection<CountData>()
        collection.findOneAndReplace(
            ::id eq this.id,
            this,
            FindOneAndReplaceOptions().upsert(true)
        )
    }

    companion object {
        private val instances: MutableList<CountData> = mutableListOf()

        fun get(channel: String): CountData? {
            return instances.firstOrNull { it.id == channel } ?: let {
                val collection = database.getCollection<CountData>()
                collection.find(CountData::id eq channel).firstOrNull()?.also { instances.add(it) }
            }
        }

        fun create(channel: String): CountData {
            val data = CountData(channel)
            instances.add(data)
            data.save()
            return data
        }
    }

}
