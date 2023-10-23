package me.aroze.snuggles.database

import com.mongodb.ConnectionString
import com.mongodb.kotlin.client.MongoClient
import com.mongodb.kotlin.client.MongoDatabase
import com.mongodb.MongoClientSettings
import me.aroze.snuggles.config.ConfigLoader.config
import me.aroze.snuggles.database.models.SnugglyStats
import java.util.*
import kotlin.concurrent.schedule

object Database {

    lateinit var database: MongoDatabase
    private lateinit var client: MongoClient

    var snugglyStats = SnugglyStats()

    fun connect() {
        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(config.authentication.mongo))
            .retryWrites(true)
            .build()

        client = MongoClient.create(settings)
        database = client.getDatabase("Snuggles")

        snugglyStats = database.getCollection<SnugglyStats>("SnugglyStats").find().firstOrNull() ?: SnugglyStats() // holy shit fwik butthole poop

        Runtime.getRuntime().addShutdownHook(Thread {
            disconnect()
            println("Database disconnected")
        })

        Timer().schedule(300000, 300000) {
            save()
        }

    }

    private fun disconnect() {
        save()
        client.close()
    }

    private fun save() {
        snugglyStats.save()
    }

}