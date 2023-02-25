package me.aroze.snuggles.utils

import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.runBlocking
import org.bson.BsonDocument
import org.bson.BsonInt32

fun MongoDatabase.ping(): Int = runBlocking {
    val start = System.currentTimeMillis()
    this@ping.runCommand(BsonDocument("ping", BsonInt32(1)))
    return@runBlocking (System.currentTimeMillis() - start).toInt()
}