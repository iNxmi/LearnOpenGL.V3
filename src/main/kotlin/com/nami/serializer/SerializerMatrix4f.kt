package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joml.Matrix4f

internal object SerializerMatrix4f : KSerializer<Matrix4f> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Matrix4f") {
            element("m00", Float.serializer().descriptor)
            element("m01", Float.serializer().descriptor)
            element("m02", Float.serializer().descriptor)
            element("m03", Float.serializer().descriptor)
            element("m10", Float.serializer().descriptor)
            element("m11", Float.serializer().descriptor)
            element("m12", Float.serializer().descriptor)
            element("m13", Float.serializer().descriptor)
            element("m20", Float.serializer().descriptor)
            element("m21", Float.serializer().descriptor)
            element("m22", Float.serializer().descriptor)
            element("m23", Float.serializer().descriptor)
            element("m30", Float.serializer().descriptor)
            element("m31", Float.serializer().descriptor)
            element("m32", Float.serializer().descriptor)
            element("m33", Float.serializer().descriptor)
        }

    override fun serialize(encoder: Encoder, value: Matrix4f) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeFloatElement(descriptor, 0, value.m00())
        composite.encodeFloatElement(descriptor, 1, value.m01())
        composite.encodeFloatElement(descriptor, 2, value.m02())
        composite.encodeFloatElement(descriptor, 3, value.m03())
        composite.encodeFloatElement(descriptor, 4, value.m10())
        composite.encodeFloatElement(descriptor, 5, value.m11())
        composite.encodeFloatElement(descriptor, 6, value.m12())
        composite.encodeFloatElement(descriptor, 7, value.m13())
        composite.encodeFloatElement(descriptor, 8, value.m20())
        composite.encodeFloatElement(descriptor, 9, value.m21())
        composite.encodeFloatElement(descriptor, 10, value.m22())
        composite.encodeFloatElement(descriptor, 11, value.m23())
        composite.encodeFloatElement(descriptor, 12, value.m30())
        composite.encodeFloatElement(descriptor, 13, value.m31())
        composite.encodeFloatElement(descriptor, 14, value.m32())
        composite.encodeFloatElement(descriptor, 15, value.m33())
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Matrix4f {
        val dec = decoder.beginStructure(descriptor)
        val matrix = Matrix4f()
        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> matrix.m00(dec.decodeFloatElement(descriptor, 0))
                1 -> matrix.m01(dec.decodeFloatElement(descriptor, 1))
                2 -> matrix.m02(dec.decodeFloatElement(descriptor, 2))
                3 -> matrix.m03(dec.decodeFloatElement(descriptor, 3))
                4 -> matrix.m10(dec.decodeFloatElement(descriptor, 4))
                5 -> matrix.m11(dec.decodeFloatElement(descriptor, 5))
                6 -> matrix.m12(dec.decodeFloatElement(descriptor, 6))
                7 -> matrix.m13(dec.decodeFloatElement(descriptor, 7))
                8 -> matrix.m20(dec.decodeFloatElement(descriptor, 8))
                9 -> matrix.m21(dec.decodeFloatElement(descriptor, 9))
                10 -> matrix.m22(dec.decodeFloatElement(descriptor, 10))
                11 -> matrix.m23(dec.decodeFloatElement(descriptor, 11))
                12 -> matrix.m30(dec.decodeFloatElement(descriptor, 12))
                13 -> matrix.m31(dec.decodeFloatElement(descriptor, 13))
                14 -> matrix.m32(dec.decodeFloatElement(descriptor, 14))
                15 -> matrix.m33(dec.decodeFloatElement(descriptor, 15))
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> error("Unexpected index: $index")
            }
        }
        dec.endStructure(descriptor)
        return matrix
    }

}