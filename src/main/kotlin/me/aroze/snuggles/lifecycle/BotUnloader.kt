package me.aroze.snuggles.lifecycle

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import me.aroze.snuggles.database.service.impl.UptimeService

/**
 * Handles safe unloading/shutdown of the bot.
 */
class BotUnloader {

    /**
     * Registers a shutdown hook that will block the JVM shutdown process until pre-shutdown tasks are completed.
     */
    fun registerDefaultShutdownHook() {
        Runtime.getRuntime().addShutdownHook(Thread{
            runBlocking {
                println("Snuggles is shutting down ;c, running pre-shutdown tasks...")
                preShutdownTasks()
                println("Safely finished pre-shutdown tasks. Byebye <3")
            }
        })
    }

    private suspend fun preShutdownTasks() {
        val stopTime = Clock.System.now()
        UptimeService.stopTrackingUptime(stopTime)
    }

}
