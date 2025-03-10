package com.nami.core.networking.tcp

import com.nami.core.networking.tcp.packet.Packet
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class Connection(
    val socket: Socket
) {

    constructor(host: String, port: Int) : this(Socket(host, port))

    private val write = BufferedWriter(OutputStreamWriter(socket.outputStream))
    private fun writeJson(json: JsonObject) {
        write.write(json.toString())
        write.newLine()
        write.flush()
    }

    fun writePacket(packet: Packet) {
        writeJson(packet.json())
    }

    private val read = BufferedReader(InputStreamReader(socket.inputStream))
    private fun readJson(): JsonObject {
        val line = read.readLine().trim()
        val json = Json.decodeFromString<JsonObject>(line)
        return json
    }

    fun readPacket(): Packet {
        val json = readJson()
        val packet = Packet(json)
        return packet
    }


    fun close() {
        write.close()
        read.close()
        socket.close()
    }

}