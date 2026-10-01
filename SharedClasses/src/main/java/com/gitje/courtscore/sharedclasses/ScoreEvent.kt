package com.gitje.courtscore.sharedclasses

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(with = ScoreEventSerializer::class)
data class ScoreEvent(
    val scoringPlayer: Int,
    val scoreAfter: ScoreSnapshot,
    val timestamp: Long = System.currentTimeMillis()
)

class ScoreEventSerializer : KSerializer<ScoreEvent> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ScoreEvent") {
            element<Player>("scoringPlayer")
            element<ScoreSnapshot>("scoreAfter")
            element<Long>("timestamp")
        }

    override fun serialize(encoder: Encoder, value: ScoreEvent) {
        val composite = encoder.beginStructure(descriptor)
        //composite.encodeSerializableElement(descriptor, 0, Player.serializer(), value.scoringPlayer)
        composite.encodeSerializableElement(descriptor, 1, ScoreSnapshot.serializer(), value.scoreAfter)
        composite.encodeLongElement(descriptor, 2, value.timestamp)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): ScoreEvent {
        var scoringPlayer = 0
        lateinit var scoreAfter: ScoreSnapshot
        var timestamp = 0L

        val composite = decoder.beginStructure(descriptor)
        loop@ while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                DECODE_DONE -> break@loop
                0 -> scoringPlayer = composite.decodeIntElement(descriptor, 0)//Player.serializer())
                1 -> scoreAfter = composite.decodeSerializableElement(descriptor, 1, ScoreSnapshot.serializer())
                2 -> timestamp = composite.decodeLongElement(descriptor, 2)
                else -> throw SerializationException("Unexpected index: $index")
            }
        }
        composite.endStructure(descriptor)
        return ScoreEvent(scoringPlayer, scoreAfter, timestamp)
    }
}