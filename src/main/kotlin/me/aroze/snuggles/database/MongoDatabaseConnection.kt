package me.aroze.snuggles.database

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import me.aroze.snuggles.database.codec.CodecRegistryManager

/**
 * Initiates a connection to a MongoDB database with the provided connection string, applying the correct settings and
 * providing helper methods to assist with database operations.
 */
class MongoDatabaseConnection(
    connectionString: String
) {

    private val client: MongoClient = MongoClient.create(MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(connectionString))
        .codecRegistry(CodecRegistryManager.constructDefaultCodecs())
        .build())

    /**
     * Retrieves a [MongoDatabase] instance for the specified database name.
     *
     * @param databaseName The name of the database to retrieve.
     * @return A MongoDatabase instance for the specified database.
     */
    fun getDatabase(databaseName: String): MongoDatabase {
        return client.getDatabase(databaseName)
    }

}
