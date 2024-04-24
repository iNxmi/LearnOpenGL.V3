package com.nami.scene.scenes

import com.nami.Window
import com.nami.constants.GamePaths
import com.nami.scene.Scene
import imgui.ImGui
import imgui.type.ImInt
import java.nio.file.Files
import kotlin.io.path.name

class WorldSelectScene : Scene() {
    override fun onInit() {

    }

    override fun onUpdate() {

    }

    override fun onRender() {

    }

    val state = ImInt()
    override fun onRenderHUD() {
        ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())

        ImGui.begin("World Select")

        val names = mutableListOf<String>()
        Files.list(GamePaths.worlds).forEach { f -> names.add(f.name) }
        ImGui.listBox("Worlds", state, names.toTypedArray())

        ImGui.button("Create")

        ImGui.end()
    }
}