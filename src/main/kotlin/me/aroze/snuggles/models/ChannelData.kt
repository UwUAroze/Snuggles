package me.aroze.snuggles.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.database.Database
import me.aroze.snuggles.database.database
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

data class ChannelData(
    val channel: String,
    val guild: String,
    val counting: CountData? = null,
    val logging: LogData? = null,
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

    companion object {
        val instances: MutableList<ChannelData> = mutableListOf()

        fun get(id: String, guild: String): ChannelData? {
            return instances.firstOrNull { it.channel == id && it.guild == guild } ?: let {
                val collection = database.getCollection<ChannelData>()
                collection.findOne(ChannelData::channel eq id, UserData::guild eq guild)?.also { instances.add(it) }
            }
        }

        fun create(id: String, guild: String): ChannelData {
            val data = get(id, guild) ?: ChannelData(id, guild)
            instances.removeIf { it.channel == id && it.guild == guild }
            instances.add(data)
            return data
        }

        @JvmStatic
        fun main(args: Array<String>) {
            ConfigLoader.load()
            Database.connect()
            val logs = database.getCollection<LogData>().find().toList()
            val counts = database.getCollection<CountData>().find().toList()

            val channels = mutableListOf<ChannelData>()
            val ids = logs.map { it.id } + counts.map { it.id }

            for (id in ids.distinct()) {
                val guild = logs.firstOrNull { it.id == id }?.guild ?: counts.firstOrNull { it.id == id }?.guild ?: let {
                    println("No guild found for $id")
                    return
                }
                channels.add(ChannelData(id, guild, counts.firstOrNull { it.id == id }, logs.firstOrNull { it.id == id }))
            }

            for (channel in channels) {
                channel.save()
            }

        }

    }

}