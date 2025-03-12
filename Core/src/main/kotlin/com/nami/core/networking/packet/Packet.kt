package com.nami.core.networking.packet

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.serializers.DefaultSerializers.EnumSerializer
import com.esotericsoftware.kryo.serializers.MapSerializer
import com.nami.core.Block
import com.nami.core.kryo.SerializerVector3i
import com.nami.core.networking.packet.request.PacketRequestChunk
import com.nami.core.networking.packet.request.PacketRequestPing
import com.nami.core.networking.packet.request.PacketRequestWorld
import com.nami.core.networking.packet.response.PacketResponseChunk
import com.nami.core.networking.packet.response.PacketResponsePing
import com.nami.core.networking.packet.response.PacketResponseWorld
import org.joml.Vector3i

interface Packet {

    companion object {

        private val packets = setOf(
            PacketRequestPing::class,
            PacketResponsePing::class,

            PacketRequestChunk::class,
            PacketResponseChunk::class,

            PacketRequestWorld::class,
            PacketResponseWorld::class
        )

        fun register(kryo: Kryo) {
            kryo.apply {

                for (packet in packets)
                    register(packet.java)

                val mapSerializer = MapSerializer().apply {
                    setKeyClass(Long::class.java, getSerializer(Long::class.java))
                    setKeysCanBeNull(false)
                }
                register(LinkedHashMap::class.java, mapSerializer)

                register(Block::class.java, EnumSerializer(Block::class.java))

                register(Vector3i::class.java, SerializerVector3i())
            }
        }

    }

}