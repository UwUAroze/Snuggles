package me.aroze.snuggles

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.aroze.snuggles.config.Config
import me.aroze.snuggles.config.TomlConfigLoader
import me.aroze.snuggles.database.MongoDatabaseConnection
import me.aroze.snuggles.database.service.impl.UptimeService
import me.aroze.snuggles.lifecycle.BotLoader
import me.aroze.snuggles.lifecycle.BotUnloader
import net.dv8tion.jda.api.JDA

lateinit var snuggles: JDA
    private set

lateinit var config: Config
    private set

lateinit var database: MongoDatabase


fun main(): Unit = runBlocking {
    val startTime = Clock.System.now()

    config = TomlConfigLoader<Config>("config.toml")
        .load()

    database = MongoDatabaseConnection(config.mongo.connectionString)
        .getDatabase(config.mongo.databaseName)

    snuggles = BotLoader(config.authentication.token)
        .loadDefault()

    BotUnloader().registerDefaultShutdownHook()

    val initialisationTime = Clock.System.now()

    println("We're snuggling <3. Logged into ${snuggles.selfUser.asTag}, finished all initialisation tasks in ${initialisationTime - startTime}")

    launch(Dispatchers.IO) {
        UptimeService.startTrackingUptime(startTime, initialisationTime)
    }

}
