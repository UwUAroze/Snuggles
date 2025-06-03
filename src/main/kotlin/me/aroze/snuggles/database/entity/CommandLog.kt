package me.aroze.snuggles.database.entity

import kotlinx.datetime.Instant
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class CommandLog(
    @BsonId
    val id: ObjectId = ObjectId(),
    val command: String,
    val arguments: Map<String, String>,
    val executorId: Long,
    val channelId: Long,
    val timestamp: Instant,
)
