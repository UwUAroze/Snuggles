package me.aroze.snuggles.database.codec

import com.mongodb.MongoClientSettings
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import java.util.ServiceLoader
import kotlin.jvm.java

/**
 * Manages codec registries.
 */
object CodecRegistryManager {

    /**
     * Constructs a default CodecRegistry that includes custom codecs loaded via ServiceLoader and the default MongoDB
     * codec registry.
     *
     * @return A CodecRegistry containing all custom codecs and the default MongoDB codecs.
     */
    fun constructDefaultCodecs(): CodecRegistry {
        val customCodecs = ServiceLoader.load(Codec::class.java, this.javaClass.classLoader).map { codec ->
            CodecRegistries.fromCodecs(codec)
        }

        return CodecRegistries.fromRegistries(
            *customCodecs.toTypedArray(),
            MongoClientSettings.getDefaultCodecRegistry()
        )
    }

}
