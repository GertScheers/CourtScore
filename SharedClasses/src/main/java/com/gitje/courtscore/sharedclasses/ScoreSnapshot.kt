package com.gitje.courtscore.sharedclasses

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ScoreSnapshotSerializer::class)
data class ScoreSnapshot(
    val set: Int,
    val points: PointScore,
    //Score of the ongoing game tennis/padel (0-15-30-40)
    val gamePoints: Pair<Int, Int> = Pair(0,0)
)

@Serializable
class PointScore(
    val opponentScore: Int,
    val myScore: Int
)

object ScoreSnapshotSerializer : KSerializer<ScoreSnapshot> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ScoreSnapshot") {
            element<Int>("set")
            element("points", PointScore.serializer().descriptor)
        }

    override fun serialize(encoder: Encoder, value: ScoreSnapshot) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeIntElement(descriptor, 0, value.set)
        composite.encodeSerializableElement(descriptor, 1, PointScore.serializer(), value.points)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): ScoreSnapshot {
        var set = 0
        lateinit var points: PointScore

        val composite = decoder.beginStructure(descriptor)
        loop@ while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> set = composite.decodeIntElement(descriptor, 0)
                1 -> points = composite.decodeSerializableElement(descriptor, 1, PointScore.serializer())
                else -> throw SerializationException("Unexpected index: $index")
            }
        }
        composite.endStructure(descriptor)
        return ScoreSnapshot(set, points)
    }
}