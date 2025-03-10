package com.nami.server

import com.nami.core.networking.tcp.Connection
import com.nami.core.networking.tcp.packet.Packet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.jsonPrimitive
import java.net.ServerSocket
import java.util.concurrent.ConcurrentHashMap

class Server {

    private val port = 63071
    private val socket = ServerSocket(port)
    private val connections = ConcurrentHashMap<String, Connection>()

    init {

        val thread = Thread {
            while (true) {
                println("Listening for incoming connections on port '$port'...")
                accept(socket)
            }
        }

        val thread2 = Thread {
            while (true) {
                Thread.sleep(1000)
                println(connections)
            }
        }

        thread.start()
        thread2.start()
    }

    private fun accept(socket: ServerSocket) {
        val connection = Connection(socket.accept())

        val read = connection.readPacket()
        if (read.type != Packet.Type.REQUEST_LOGIN) {
            println("Connection refused! $read")
            connection.close()
            return
        }

        connections[read.data["username"]!!.jsonPrimitive.content] = connection

        val write = Packet(Packet.Type.ACCEPT_LOGIN)
        connection.writePacket(write)
    }

}