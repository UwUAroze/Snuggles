package me.aroze.snuggles.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.database.database
import org.litote.kmongo.eq
import org.litote.kmongo.getCollection

data class LogData(
    var id: String,
    val guild: String,

    var disabled: Boolean = false,

    var logMessageChanges: Boolean = true,
) {

    @JsonIgnore
    fun save() {
        val collection = database.getCollection<LogData>()
        collection.findOneAndReplace(
            ::id eq this.id,
            this,
            FindOneAndReplaceOptions().upsert(true)
        )
    }

    @JsonIgnore
    fun getSelectedOptions() : List<String> {
        val options = mutableListOf<String>()
        if (logMessageChanges) options.add("Log message changes")
        return options
    }

    companion object {

        val instances: MutableList<LogData> = mutableListOf()

        fun getByGuild(guild: String): LogData? {
            return LogData.instances.firstOrNull { it.guild == guild } ?: let {
                val collection = database.getCollection<LogData>()
                collection.find(CountData::guild eq guild).firstOrNull()?.also { LogData.instances.add(it) }
            }
        }

        fun create(channel: String, guild: String): LogData {
            val data = LogData.getByGuild(guild) ?: LogData(channel, guild)
            data.id = channel
            data.disabled = false
            LogData.instances.removeIf { it.guild == guild }
            LogData.instances.add(data)
            return data
        }

    }
}
