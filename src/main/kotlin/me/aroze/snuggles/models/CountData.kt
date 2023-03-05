package me.aroze.snuggles.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.database.database
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import org.bson.codecs.pojo.annotations.BsonProperty
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection

data class CountData(
    var id: String,
    val guild: String,

    var lastCounter: String = "",
    var count: Int = 0,

    var disabled: Boolean = false,

    @BsonProperty("consecutive_users")
    var allowConsecutiveUsers: Boolean = false,

    @BsonProperty("allow_talking")
    var allowTalking: Boolean = true

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
    fun getSelectedOptions() : List<SelectOption> {
        val options = mutableListOf<SelectOption>()
        if (allowConsecutiveUsers) options.add(SelectOption.of("Consecutive counting", "consecutive-counting"))
        if (allowTalking) options.add(SelectOption.of("Allow speaking", "allow-speaking"))
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
