package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object SerializerClosedFloatingPointRangeFloat : KSerializer<ClosedFloatingPointRange<Float>> {

    override val descriptor = buildClassSerialDescriptor("ClosedFloatingPointRangeFloat") {
        element("start", Float.serializer().descriptor)
        element("endInclusive", Float.serializer().descriptor)
    }

    override fun serialize(encoder: Encoder, value: ClosedFloatingPointRange<Float>) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeFloatElement(descriptor, 0, value.start)
        composite.encodeFloatElement(descriptor, 1, value.endInclusive)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): ClosedFloatingPointRange<Float> {
        val dec = decoder.beginStructure(descriptor)

        var start = 0f
        var endInclusive = 0f

        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> start = dec.decodeFloatElement(descriptor, 0)
                1 -> endInclusive = dec.decodeFloatElement(descriptor, 1)
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> error("Unexpected index: $index")
            }
        }

        dec.endStructure(descriptor)
        return start..endInclusive
    }

}