package me.aroze.snuggles.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.database.database
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

data class ChannelData(
    val channel: String,
    val guild: String,
    var counting: CountData? = null,
    var logging: LogData? = null,
) {

    @JsonIgnore
    fun save() {
        val collection = database.getCollection<ChannelData>()
        collection.findOneAndReplace(
            ::channel eq this.channel,
            this,
            FindOneAndReplaceOptions().upsert(true)
        )
    }

    @JsonIgnore
    fun delete() {
        val collection = database.getCollection<ChannelData>()
        collection.deleteOne(::channel eq this.channel)
        ChannelData.instances.remove(this)
    }

    @JsonIgnore
    fun createCounting(): CountData {
        if (counting != null) counting!!.disabled = false
        else counting = CountData()
        return counting!!
    }

    @JsonIgnore
    fun createLogging(): LogData {
        if (logging != null) logging!!.disabled = false
        else logging = LogData()
        return logging!!
    }

    companion object {
        val instances: MutableList<ChannelData> = mutableListOf()

        fun getByChannel(id: String): ChannelData? {
            return instances.firstOrNull { it.channel == id } ?: let {
                val collection = database.getCollection<ChannelData>()
                collection.findOne(ChannelData::channel eq id)?.also { instances.add(it) }
            }
        }

        fun getByGuild(id: String): List<ChannelData> {
            return instances.filter { it.guild == id } + let {
                val collection = database.getCollection<ChannelData>()
                collection.find(ChannelData::guild eq id).toList().also { instances.addAll(it) }
            }
        }

        fun create(id: String, guild: String): ChannelData {
            val data = getByChannel(id) ?: ChannelData(id, guild)
            instances.removeIf { it.channel == id }
            instances.add(data)
            return data
        }

    }

}