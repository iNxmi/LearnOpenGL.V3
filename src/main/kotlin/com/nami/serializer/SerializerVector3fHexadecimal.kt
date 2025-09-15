package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joml.Vector3f

internal object SerializerVector3fHexadecimal : KSerializer<Vector3f> {

    override val descriptor = PrimitiveSerialDescriptor("Vector3fHexadecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Vector3f) {
        val r = (value.x * 255.0f).toInt()
        val g = (value.y * 255.0f).toInt()
        val b = (value.z * 255.0f).toInt()

        val color = (r shl 16) + (g shl 8) + b
        val hexadecimal = Integer.toHexString(color)

        encoder.encodeString(hexadecimal)
    }

    override fun deserialize(decoder: Decoder): Vector3f {
        val color = Integer.parseInt(decoder.decodeString(), 16)

        val r = (color shr 16) and 0xFF
        val g = (color shr 8) and 0xFF
        val b = (color shr 0) and 0xFF

        return Vector3f(
            r.toFloat(),
            g.toFloat(),
            b.toFloat()
        ).div(255.0f)
    }

}