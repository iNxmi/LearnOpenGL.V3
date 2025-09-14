package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joml.Vector4f

class SerializerVector4fHexadecimal : KSerializer<Vector4f> {

    override val descriptor = PrimitiveSerialDescriptor("Vector4fHexadecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Vector4f) {
        val r = (value.x * 255.0f).toInt()
        val g = (value.y * 255.0f).toInt()
        val b = (value.z * 255.0f).toInt()
        val a = (value.w * 255.0f).toInt()

        val color = (a shl 24) + (r shl 16) + (g shl 8) + b
        val hexadecimal = Integer.toHexString(color)

        encoder.encodeString(hexadecimal)
    }

    override fun deserialize(decoder: Decoder): Vector4f {
        val color: Int = Integer.parseInt(decoder.decodeString(), 16)

        val a = (color shr 24) and 0xFF
        val r = (color shr 16) and 0xFF
        val g = (color shr 8) and 0xFF
        val b = (color shr 0) and 0xFF

        return Vector4f(
            r.toFloat(),
            g.toFloat(),
            b.toFloat(),
            a.toFloat()
        ).div(255.0f)
    }

}