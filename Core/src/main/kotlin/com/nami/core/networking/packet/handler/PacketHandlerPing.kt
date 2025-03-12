package com.nami.core.networking.packet.handler

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.nami.core.networking.packet.request.PacketRequestPing
import com.nami.core.networking.packet.response.PacketResponsePing
import mu.KotlinLogging

class PacketHandlerPing : Listener() {

    private val log = KotlinLogging.logger {}

    override fun received(connection: Connection, packet: Any) {
        if (packet !is PacketRequestPing && packet !is PacketResponsePing)
            return

        when (packet) {
            is PacketRequestPing -> {
                val response = PacketResponsePing(
                    timeMsRequest = packet.timeMsRequest, timeMsResponse = System.currentTimeMillis()
                )
                connection.sendTCP(response)
            }

            is PacketResponsePing -> {
                val timeMsRequest = packet.timeMsRequest
                val timeMsResponse = packet.timeMsResponse
                val timeMsCurrent = System.currentTimeMillis()

                val timeMsToReceiver = timeMsResponse - timeMsRequest
                val timeMsToSender = timeMsCurrent - timeMsResponse
                val timeMsTotal = timeMsToReceiver + timeMsToSender

                log.info { "Ping To($timeMsToReceiver) From($timeMsToSender) Total($timeMsTotal)" }
            }
        }
    }

}