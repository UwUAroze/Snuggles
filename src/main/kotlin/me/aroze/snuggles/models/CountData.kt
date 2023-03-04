package me.aroze.snuggles.models

import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.database.database
import org.bson.codecs.pojo.annotations.BsonIgnore
import org.bson.codecs.pojo.annotations.BsonProperty
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection

data class CountData(
    var id: String,
    val guild: String,

    var lastCounter: String = "",
    var count: Int = 0,

    @BsonProperty("consecutive_users")
    var allowConsecutiveUsers: Boolean = false,

    @BsonProperty("allow_talking")
    var allowTalking: Boolean = true

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

    @BsonIgnore
    fun delete() {
        val collection = database.getCollection<CountData>()
        collection.deleteOne(::id eq this.id)
        instances.remove(this)
    }

    @BsonIgnore()
    fun resetCurrentCount() {
        count = 0
        lastCounter = ""
    }

    companion object {
        val instances: MutableList<CountData> = mutableListOf()

        fun getByChannel(channel: String): CountData? {
            return instances.firstOrNull { it.id == channel } ?: let {
                val collection = database.getCollection<CountData>()
                collection.find(CountData::id eq channel).firstOrNull()?.also { instances.add(it) }
            }
        }

        fun getByGuild(guild: String): CountData? {
            return instances.firstOrNull { it.guild == guild } ?: let {
                val collection = database.getCollection<CountData>()
                collection.find(CountData::guild eq guild).firstOrNull()?.also { instances.add(it) }
            }
        }

        fun create(channel: String, guild: String): CountData {
            val data = getByGuild(guild) ?: CountData(channel, guild)
            data.id = channel
            instances.removeIf { it.guild == guild }
            instances.add(data)
            return data
        }
    }

}
