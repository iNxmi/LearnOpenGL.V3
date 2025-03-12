package com.nami.server

import com.esotericsoftware.kryo.serializers.DefaultSerializers.EnumSerializer
import com.esotericsoftware.kryo.serializers.MapSerializer
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import com.esotericsoftware.minlog.Log
import com.nami.core.Block
import com.nami.core.networking.packet.Packet
import com.nami.core.networking.packet.handler.PacketHandlerPing
import com.nami.core.networking.packet.request.PacketRequestChunk
import com.nami.core.networking.packet.request.PacketRequestPing
import com.nami.core.networking.packet.request.PacketRequestWorld
import com.nami.core.networking.packet.response.PacketResponseChunk
import com.nami.core.networking.packet.response.PacketResponsePing
import com.nami.core.networking.packet.response.PacketResponseWorld
import mu.KotlinLogging
import org.joml.Vector3i

class GameServer : Listener() {

    private val log = KotlinLogging.logger {}

    private val port = 63071
    private val server = Server()

    private val world = World(
        Vector3i(16, 16, 16),
        System.currentTimeMillis(),
        64
    )

    init {
        Log.set(Log.LEVEL_NONE)

        Packet.register(server.kryo)

        server.addListener(this)
        server.addListener(PacketHandlerPing())

        server.start()
        server.bind(port)
    }

    override fun connected(connection: Connection) {
        log.info { "$connection has connected." }
    }

    override fun received(connection: Connection, packet: Any) {
        if (packet !is Packet)
            return

        log.info { "Received a packet ($packet)" }

        when (packet) {
            is PacketRequestWorld -> {
                val response = PacketResponseWorld(world.size, world.seed, world.waterLevel)
                connection.sendTCP(response)
            }

            is PacketRequestChunk -> {
                val id = packet.id
                val chunk = world.getChunk(id)

                val response = PacketResponseChunk(chunk.getBlocks())
                connection.sendTCP(response)
            }
        }

    }

    override fun disconnected(connection: Connection) {
        log.warn { "$connection has disconnected." }
    }

}