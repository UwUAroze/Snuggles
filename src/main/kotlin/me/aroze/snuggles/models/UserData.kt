package me.aroze.snuggles.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.database.database
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

data class UserData(
    val id: String,
    val guild: String,

    var totalCounts: Int = 0
) {

    @JsonIgnore
    fun save() {
        val collection = database.getCollection<UserData>()
        collection.findOneAndReplace(
            ::id eq this.id,
            this,
            FindOneAndReplaceOptions().upsert(true)
        )
    }

    companion object {

        val instances: MutableList<UserData> = mutableListOf()

        fun get(id: String, guild: String): UserData? {
            return instances.firstOrNull { it.id == id && it.guild == guild } ?: let {
                val collection = database.getCollection<UserData>()
                collection.findOne(UserData::id eq id, UserData::guild eq guild)?.also { UserData.instances.add(it) }
            }
        }

        fun create(id: String, guild: String): UserData {
            val data = UserData.get(id, guild) ?: UserData(id, guild)
            instances.removeIf { it.id == id && it.guild == guild }
            instances.add(data)
            return data
        }

    }

}