package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joml.Vector3i

class SerializerVector3i : KSerializer<Vector3i> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vector3i") {
            element("x", Int.serializer().descriptor)
            element("y", Int.serializer().descriptor)
            element("z", Int.serializer().descriptor)
        }

    override fun serialize(encoder: Encoder, value: Vector3i) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeIntElement(descriptor, 0, value.x)
        composite.encodeIntElement(descriptor, 1, value.y)
        composite.encodeIntElement(descriptor, 2, value.z)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Vector3i {
        val dec = decoder.beginStructure(descriptor)
        val vector = Vector3i()
        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> vector.x = dec.decodeIntElement(descriptor, 0)
                1 -> vector.y = dec.decodeIntElement(descriptor, 1)
                2 -> vector.z = dec.decodeIntElement(descriptor, 2)
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> error("Unexpected index: $index")
            }
        }
        dec.endStructure(descriptor)
        return vector
    }

}