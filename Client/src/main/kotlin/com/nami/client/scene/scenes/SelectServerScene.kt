package com.nami.client.scene.scenes

import com.nami.client.scene.Scene
import com.nami.client.scene.SceneManager
import com.nami.core.networking.tcp.Connection
import com.nami.core.networking.tcp.packet.Packet
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImInt
import imgui.type.ImString
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class SelectServerScene : Scene {

    override fun enable() {

    }

    override fun update() {

    }

    override fun render() {

    }

    private var connection: Connection? = null

    private val hostname = ImString("127.0.0.1")
    private val port = ImInt(63071)
    private val username = ImString("Memphis")

    override fun renderHUD() {
        ImGui.setNextWindowPos(0f, 0f)
        ImGui.setNextWindowSize(1920f, 1080f)

        ImGui.getFont().scale = 2.5f
        ImGui.begin("Multiplayer", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

        ImGui.inputText("Hostname", hostname)
        ImGui.inputInt("Port", port)
        if (ImGui.button("Connect"))
            connection = Connection(hostname.get(), port.get())

        ImGui.inputText("Username", username)
        if (ImGui.button("Send Login Packet"))
            if (connection != null) {
                val write = Packet(Packet.Type.REQUEST_LOGIN, buildJsonObject {
                    put("username", JsonPrimitive(username.get()))
                })
                connection!!.writePacket(write)

                val read = connection!!.readPacket()
                if (read.type == Packet.Type.SUCCESS_LOGIN)
                    SceneManager.set()
            }

        ImGui.end()
    }

    override fun disable() {

    }

}