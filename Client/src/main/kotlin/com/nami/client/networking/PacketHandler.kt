package com.nami.client.networking

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.nami.core.networking.packet.Packet
import mu.KotlinLogging

class PacketHandler : Listener() {

    private val log = KotlinLogging.logger {}

    override fun connected(connection: Connection) {
        log.info { "Connected to Server ($connection)" }
    }

    override fun disconnected(connection: Connection) {
        log.warn { "Disconnected from Server ($connection)" }
    }

    override fun received(connection: Connection, packet: Any) {
        if(packet !is Packet)
            return

        log.info { "Received a packet ($packet)" }
    }

}