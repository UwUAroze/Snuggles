package me.aroze.snuggles.database

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aroze.snuggles.config.ConfigLoader
import me.aroze.snuggles.models.BotStats
import me.aroze.snuggles.models.ChannelData
import me.aroze.snuggles.models.LoggedMessage
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

lateinit var database: MongoDatabase

object Database {
    private lateinit var client: MongoClient

    var botStats = BotStats()

    fun connect() = runBlocking {
        client = KMongo.createClient(ConfigLoader.config.getString("authentication.mongo"))
        database = client.getDatabase("Snuggles")

        val statsCollection = database.getCollection<BotStats>()

        launch {
            LoggedMessage.invalidateOld()
            botStats = statsCollection.find().first() ?: return@launch
        }
    }

    fun save() {
        botStats.save()
        for (channelData in ChannelData.instances) channelData.save()
    }

    fun invalidateCache() {
        LoggedMessage.invalidateOld()
        for (channelData in ChannelData.instances) channelData.save()
        ChannelData.instances.clear()
    }

    fun disconnect() {
        save()
        client.close()
    }

}