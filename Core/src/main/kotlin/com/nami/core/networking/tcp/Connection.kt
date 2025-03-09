package com.nami.core.networking.tcp

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class Connection(
    private val socket: Socket
) {

    constructor(host: String, port: Int) : this(Socket(host, port))

    private val write = BufferedWriter(OutputStreamWriter(socket.outputStream))
    fun write(json: JsonObject) {
        write.write(json.toString())
        write.newLine()
        write.flush()
    }

    private val read = BufferedReader(InputStreamReader(socket.inputStream))
    fun read(): JsonObject {
        val line = read.readLine().trim()
        val json = Json.decodeFromString<JsonObject>(line)
        return json
    }

    fun close() {
        write.close()
        read.close()
        socket.close()
    }

}