package me.aroze.snuggles.database.codec.impl

import com.google.auto.service.AutoService
import kotlinx.datetime.Instant
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

@AutoService(Codec::class)
class InstantCodec : Codec<Instant> {
    override fun encode(writer: BsonWriter, value: Instant, encoderContext: EncoderContext) {
        writer.writeDateTime(value.toEpochMilliseconds())
    }

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): Instant {
        val epochMilliseconds = reader.readDateTime()
        return Instant.Companion.fromEpochMilliseconds(epochMilliseconds)
    }

    override fun getEncoderClass(): Class<Instant> = Instant::class.java
}
