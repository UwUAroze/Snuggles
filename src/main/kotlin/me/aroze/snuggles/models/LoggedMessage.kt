package me.aroze.snuggles.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mongodb.client.model.FindOneAndReplaceOptions
import me.aroze.snuggles.database.database
import org.litote.kmongo.*

data class LoggedMessage (
    val createdAt: Long,
    val message: String,
    val guild: String,
    val author: String,
    val content: String,
    val attachments: MutableList<AttachmentInfo> = mutableListOf(),
) {

    @JsonIgnore
    fun save() {
        val collection = database.getCollection<LoggedMessage>()
        collection.findOneAndReplace(
            ::message eq this.message,
            this,
            FindOneAndReplaceOptions().upsert(true)
        )
    }

    @JsonIgnore
    fun delete() {
        val collection = database.getCollection<LoggedMessage>()
        collection.deleteOne(::message eq this.message)
    }

    @JsonIgnore
    fun edit(content: String) {
        val collection = database.getCollection<LoggedMessage>()
        collection.updateOne (
            ::message eq this.message,
            ::content setTo content
        )
    }

    companion object {

        fun invalidateOld(days: Int = 7) {
            val collection = database.getCollection<LoggedMessage>()
            val now = System.currentTimeMillis()
            collection.deleteMany(LoggedMessage::createdAt lt (now - 1000 * 60 * 60 * 24 * days))
        }

        fun getByMessageId(id: String) : LoggedMessage? {
            val collection = database.getCollection<LoggedMessage>()
            return collection.findOne(LoggedMessage::message eq id)
        }

    }

    data class AttachmentInfo(
        val url: String,
        val name: String
    )

}