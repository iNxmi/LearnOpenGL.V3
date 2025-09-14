package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joml.Vector4i

class SerializerVector4iHexadecimal : KSerializer<Vector4i> {

    override val descriptor = PrimitiveSerialDescriptor("Vector4iHexadecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Vector4i) {
        val r = value.x
        val g = value.y
        val b = value.z
        val a = value.w

        val color = (a shl 24) + (r shl 16) + (g shl 8) + b
        val hexadecimal = Integer.toHexString(color)

        encoder.encodeString(hexadecimal)
    }

    override fun deserialize(decoder: Decoder): Vector4i {
        val hexadecimal = decoder.decodeString()
        val color = Integer.parseInt(hexadecimal, 16)

        val a = (color shr 24) and 0xFF
        val r = (color shr 16) and 0xFF
        val g = (color shr 8) and 0xFF
        val b = (color shr 0) and 0xFF

        return Vector4i(r, g, b, a)
    }

}