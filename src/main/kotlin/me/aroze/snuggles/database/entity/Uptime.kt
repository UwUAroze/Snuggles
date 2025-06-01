package me.aroze.snuggles.database.entity

import kotlinx.datetime.Instant
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Uptime(
    @BsonId
    val id: ObjectId = ObjectId(),
    val startedAt: Instant,
    val initializedAt: Instant,
    val endedAt: Instant? = null,
)
