package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joml.Vector3i

class SerializerVector3iHexadecimal : KSerializer<Vector3i> {

    override val descriptor = PrimitiveSerialDescriptor("Vector3iHexadecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Vector3i) {
        val r = value.x
        val g = value.y
        val b = value.z

        val color = (r shl 16) + (g shl 8) + b
        val hexadecimal = Integer.toHexString(color)

        encoder.encodeString(hexadecimal)
    }

    override fun deserialize(decoder: Decoder): Vector3i {
        val hexadecimal = decoder.decodeString()
        val color = Integer.parseInt(hexadecimal, 16)

        val r = (color shr 16) and 0xFF
        val g = (color shr 8) and 0xFF
        val b = (color shr 0) and 0xFF

        return Vector3i(r, g, b)
    }

}