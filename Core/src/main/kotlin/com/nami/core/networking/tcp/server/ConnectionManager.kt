package com.nami.core.networking.tcp.server

import com.nami.core.networking.tcp.Connection
import java.net.ServerSocket

class ConnectionManager(
    port: Int
) {

    private val socket = ServerSocket(port)

    fun accept(): Connection {
        return Connection(socket.accept())
    }

}