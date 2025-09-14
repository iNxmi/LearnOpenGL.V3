package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joml.Vector2i

class SerializerVector2i : KSerializer<Vector2i> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vector2i") {
            element("x", Int.serializer().descriptor)
            element("y", Int.serializer().descriptor)
        }

    override fun serialize(encoder: Encoder, value: Vector2i) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeIntElement(descriptor, 0, value.x)
        composite.encodeIntElement(descriptor, 1, value.y)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Vector2i {
        val dec = decoder.beginStructure(descriptor)
        val vector = Vector2i()
        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> vector.x = dec.decodeIntElement(descriptor, 0)
                1 -> vector.y = dec.decodeIntElement(descriptor, 1)
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> error("Unexpected index: $index")
            }
        }
        dec.endStructure(descriptor)
        return vector
    }

}