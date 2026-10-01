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

@Serializable(with = PlayerSerializer::class)
class Player(val name: String)

class PlayerSerializer : KSerializer<Player> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Player") {
            element<String>("name")
        }

    override fun serialize(encoder: Encoder, value: Player) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeStringElement(descriptor, 0, value.name)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Player {
        var name = ""
        val composite = decoder.beginStructure(descriptor)
        loop@ while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> name = composite.decodeStringElement(descriptor, 0)
                else -> throw SerializationException("Unexpected index: $index")
            }
        }
        composite.endStructure(descriptor)
        return Player(name)
    }
}