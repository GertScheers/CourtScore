package com.gitje.courtscore.sharedclasses

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ScoreSnapshotSerializer::class)
data class ScoreSnapshot(
    val set: Int,
    // TODO: Double-check 0: Opponent 1: Self
    val points: Pair<Int, Int>
)

object ScoreSnapshotSerializer : KSerializer<ScoreSnapshot> {
    private val pairSerializer = PairSerializer(Int.serializer(), Int.serializer())

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ScoreSnapshot") {
            element<Int>("set")
            element("points", pairSerializer.descriptor)
        }

    override fun serialize(encoder: Encoder, value: ScoreSnapshot) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeIntElement(descriptor, 0, value.set)
        composite.encodeSerializableElement(descriptor, 1, pairSerializer, value.points)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): ScoreSnapshot {
        var set = 0
        lateinit var points: Pair<Int, Int>

        val composite = decoder.beginStructure(descriptor)
        loop@ while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> set = composite.decodeIntElement(descriptor, 0)
                1 -> points = composite.decodeSerializableElement(descriptor, 1, pairSerializer)
                else -> throw SerializationException("Unexpected index: $index")
            }
        }
        composite.endStructure(descriptor)
        return ScoreSnapshot(set, points)
    }
}