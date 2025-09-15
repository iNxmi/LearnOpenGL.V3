package com.nami.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object SerializerIntRange : KSerializer<IntRange> {

    override val descriptor = buildClassSerialDescriptor("IntRange") {
        element("start", Int.serializer().descriptor)
        element("endInclusive", Int.serializer().descriptor)
    }

    override fun serialize(encoder: Encoder, value: IntRange) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeIntElement(descriptor, 0, value.start)
        composite.encodeIntElement(descriptor, 1, value.endInclusive)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): IntRange {
        val dec = decoder.beginStructure(descriptor)

        var start = 0
        var endInclusive = 0

        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                0 -> start = dec.decodeIntElement(descriptor, 0)
                1 -> endInclusive = dec.decodeIntElement(descriptor, 1)
                CompositeDecoder.DECODE_DONE -> break@loop
                else -> error("Unexpected index: $index")
            }
        }

        dec.endStructure(descriptor)
        return IntRange(start, endInclusive)
    }

}