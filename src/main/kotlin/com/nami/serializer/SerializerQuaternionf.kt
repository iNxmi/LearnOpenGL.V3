package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joml.Quaternionf

class SerializerQuaternionf: KSerializer<Quaternionf> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Quaternionf") {
            element("x", Float.serializer().descriptor)
            element("y", Float.serializer().descriptor)
            element("z", Float.serializer().descriptor)
            element("w", Float.serializer().descriptor)
        }

    override fun serialize(encoder: Encoder, value: Quaternionf) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeFloatElement(descriptor, 0, value.x)
        composite.encodeFloatElement(descriptor, 1, value.y)
        composite.encodeFloatElement(descriptor, 2, value.z)
        composite.encodeFloatElement(descriptor, 3, value.w)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Quaternionf {
        val dec = decoder.beginStructure(descriptor)
        val quaternion = Quaternionf()
        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> quaternion.x = dec.decodeFloatElement(descriptor, 0)
                1 -> quaternion.y = dec.decodeFloatElement(descriptor, 1)
                2 -> quaternion.z = dec.decodeFloatElement(descriptor, 2)
                3 -> quaternion.w = dec.decodeFloatElement(descriptor, 3)
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> error("Unexpected index: $index")
            }
        }
        dec.endStructure(descriptor)
        return quaternion
    }


}