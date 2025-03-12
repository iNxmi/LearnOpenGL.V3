package com.nami.client.scene.scenes

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.minlog.Log
import com.nami.client.networking.PacketHandler
import com.nami.client.scene.Scene
import com.nami.client.scene.SceneManager
import com.nami.core.asID
import com.nami.core.networking.packet.Packet
import com.nami.core.networking.packet.handler.PacketHandlerPing
import com.nami.core.networking.packet.request.PacketRequestChunk
import com.nami.core.networking.packet.request.PacketRequestPing
import com.nami.core.networking.packet.request.PacketRequestWorld
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImInt
import imgui.type.ImString
import org.joml.Vector3i
import java.io.IOException

class SelectServerScene : Scene {

    private val client = Client(1024 * 16, 1024 * 16)

    private val hostname = ImString("127.0.0.1")
    private val port = ImInt(63071)
    private val username = ImString("Memphis")
    private var error = ""

    init {
        Log.set(Log.LEVEL_NONE)

        Packet.register(client.kryo)

        client.addListener(PacketHandler())
        client.addListener(PacketHandlerPing())

        client.start()
    }

    override fun enable() {

    }

    override fun update() {

    }

    override fun render() {

    }

    override fun renderHUD() {
        ImGui.setNextWindowPos(0f, 0f)
        ImGui.setNextWindowSize(1920f, 1080f)

        ImGui.getFont().scale = 2.5f
        ImGui.begin("Multiplayer", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

        ImGui.inputText("Hostname", hostname)
        ImGui.inputInt("Port", port)
        if (error.isNotBlank())
            ImGui.text("Error: $error")
        if (ImGui.button("Connect")) {
            try {
                client.connect(5000, hostname.get(), port.get())
//                SceneManager.set(ServerScene(client))
                error = ""
            } catch (e: IOException) {
                error = "Can't 'connect to ${hostname.get()}:${port.get()}"
            }
        }

        ImGui.separator()

        if (client.isConnected) {
            if (ImGui.button("Send Ping Packet")) {
                val request = PacketRequestPing(timeMsRequest = System.currentTimeMillis())
                client.sendTCP(request)
            }

            if (ImGui.button("Send World Request")) {
                val request = PacketRequestWorld()
                client.sendTCP(request)
            }

            if (ImGui.button("Send Chunk Request Packet (0, 0, 0)")) {
                val request = PacketRequestChunk(id = Vector3i(0, 0, 0).asID(Vector3i(16, 16, 16)))
                client.sendTCP(request)
            }

            if (ImGui.button("Disconnect")) {
                client.close()
            }

        }

        ImGui.end()
    }

    override fun disable() {

    }

}