package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joml.Matrix3f

class SerializerMatrix3f : KSerializer<Matrix3f> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Matrix3f") {
            element("m00", Float.serializer().descriptor)
            element("m01", Float.serializer().descriptor)
            element("m02", Float.serializer().descriptor)
            element("m10", Float.serializer().descriptor)
            element("m11", Float.serializer().descriptor)
            element("m12", Float.serializer().descriptor)
            element("m20", Float.serializer().descriptor)
            element("m21", Float.serializer().descriptor)
            element("m22", Float.serializer().descriptor)
        }

    override fun serialize(encoder: Encoder, value: Matrix3f) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeFloatElement(descriptor, 0, value.m00())
        composite.encodeFloatElement(descriptor, 1, value.m01())
        composite.encodeFloatElement(descriptor, 2, value.m02())
        composite.encodeFloatElement(descriptor, 3, value.m10())
        composite.encodeFloatElement(descriptor, 4, value.m11())
        composite.encodeFloatElement(descriptor, 5, value.m12())
        composite.encodeFloatElement(descriptor, 6, value.m20())
        composite.encodeFloatElement(descriptor, 7, value.m21())
        composite.encodeFloatElement(descriptor, 8, value.m22())
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Matrix3f {
        val dec = decoder.beginStructure(descriptor)
        val matrix = Matrix3f()
        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> matrix.m00(dec.decodeFloatElement(descriptor, 0))
                1 -> matrix.m01(dec.decodeFloatElement(descriptor, 1))
                2 -> matrix.m02(dec.decodeFloatElement(descriptor, 2))
                3 -> matrix.m10(dec.decodeFloatElement(descriptor, 3))
                4 -> matrix.m11(dec.decodeFloatElement(descriptor, 4))
                5 -> matrix.m12(dec.decodeFloatElement(descriptor, 5))
                6 -> matrix.m20(dec.decodeFloatElement(descriptor, 6))
                7 -> matrix.m21(dec.decodeFloatElement(descriptor, 7))
                8 -> matrix.m22(dec.decodeFloatElement(descriptor, 8))
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> error("Unexpected index: $index")
            }
        }
        dec.endStructure(descriptor)
        return matrix
    }

}