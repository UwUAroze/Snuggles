package me.aroze.snuggles.database.service.impl

import me.aroze.snuggles.database.entity.CommandLog
import me.aroze.snuggles.database.service.DatabaseCollection

object CommandLogService : DatabaseCollection<CommandLog>(CommandLog::class) {

    suspend fun logCommand(commandLog: CommandLog) {
        collection.insertOne(commandLog)
    }

}
