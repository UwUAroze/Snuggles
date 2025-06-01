package me.aroze.snuggles.database.service.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.datetime.Instant
import me.aroze.snuggles.database.entity.Uptime
import me.aroze.snuggles.database.service.DatabaseCollection
import org.bson.BsonValue

object UptimeService : DatabaseCollection<Uptime>(Uptime::class) {

    private var ongoingUptimeTracker: BsonValue? = null

    suspend fun startTrackingUptime(startedAt: Instant, initializedAt: Instant) {
        if (ongoingUptimeTracker != null) error("Attempted to track uptime twice")

        val uptime = Uptime(
            startedAt = startedAt,
            initializedAt = initializedAt,
        )

        val result = collection.insertOne(uptime)
        ongoingUptimeTracker = result.insertedId
    }

    suspend fun stopTrackingUptime(stoppedAt: Instant) {
        if (ongoingUptimeTracker == null) error("Attempted to record stop time without an ongoing uptime tracker")

        val filter = Filters.eq("_id", ongoingUptimeTracker)
        val update = Updates.set(Uptime::endedAt.name, stoppedAt)

        collection.updateOne(filter, update)
        ongoingUptimeTracker = null
    }

}
