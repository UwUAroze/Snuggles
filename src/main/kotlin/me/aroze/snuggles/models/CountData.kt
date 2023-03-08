package me.aroze.snuggles.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.database.database
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection

data class CountData(
    var id: String,
    val guild: String,

    var lastCounter: String = "",
    var count: Int = 0,
    var highScore: Int = 0,

    var disabled: Boolean = false,

    var allowConsecutiveUsers: Boolean = false,
    var allowTalking: Boolean = true,
    var kinderMessages: Boolean = false,

    ) {

    @JsonIgnore
    fun save() {
        val collection = database.getCollection<CountData>()
        collection.findOneAndReplace(
            ::id eq this.id,
            this,
            FindOneAndReplaceOptions().upsert(true)
        )
    }

    @JsonIgnore
    fun delete() {
        val collection = database.getCollection<CountData>()
        collection.deleteOne(::id eq this.id)
        instances.remove(this)
    }

    @JsonIgnore
    fun resetCurrentCount() {
        count = 0
        lastCounter = ""
    }

    @JsonIgnore
    fun getSelectedOptions() : List<String> {
        val options = mutableListOf<String>()
        if (allowConsecutiveUsers) options.add("Consecutive counting")
        if (allowTalking) options.add("Allow speaking")
        return options
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
            data.disabled = false
            instances.removeIf { it.guild == guild }
            instances.add(data)
            return data
        }
    }

}
