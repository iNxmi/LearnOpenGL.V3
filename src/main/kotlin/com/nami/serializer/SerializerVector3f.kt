package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joml.Vector3f

class SerializerVector3f : KSerializer<Vector3f> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Vector3f") {
            element("x", Float.serializer().descriptor)
            element("y", Float.serializer().descriptor)
            element("z", Float.serializer().descriptor)
        }

    override fun serialize(encoder: Encoder, value: Vector3f) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeFloatElement(descriptor, 0, value.x)
        composite.encodeFloatElement(descriptor, 1, value.y)
        composite.encodeFloatElement(descriptor, 2, value.z)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Vector3f {
        val dec = decoder.beginStructure(descriptor)
        val vector = Vector3f()
        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> vector.x = dec.decodeFloatElement(descriptor, 0)
                1 -> vector.y = dec.decodeFloatElement(descriptor, 1)
                2 -> vector.z = dec.decodeFloatElement(descriptor, 2)
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> error("Unexpected index: $index")
            }
        }
        dec.endStructure(descriptor)
        return vector
    }

}