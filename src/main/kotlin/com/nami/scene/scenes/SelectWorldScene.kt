package com.nami.scene.scenes

import com.nami.Window
import com.nami.resources.GamePath
import com.nami.scene.Scene
import com.nami.scene.SceneManager
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImInt
import imgui.type.ImString
import java.nio.file.Files
import kotlin.io.path.name

class SelectWorldScene : Scene() {
    override fun onInit() {

    }

    override fun onUpdate() {

    }

    override fun onRender() {

    }

    val state = ImInt()
    val seed = ImString(System.currentTimeMillis().toString())
    override fun onRenderHUD() {
        ImGui.setNextWindowPos(0f, 0f)
        ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())

        ImGui.begin("Select World", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

        val names = mutableListOf<String>()
        Files.list(GamePath.worlds).forEach { f -> names.add(f.name) }
        ImGui.listBox("Worlds", state, names.toTypedArray())

        ImGui.separator()
        ImGui.separator()
        ImGui.separator()
        ImGui.separator()
        ImGui.separator()

        if (ImGui.inputText("Seed", seed))
            if (!seed.get().all { char -> char.isDigit() })
                seed.set("")

        if (ImGui.button("Create"))
            SceneManager.selected = PlayScene(seed.get().toLong())

        ImGui.end()
    }
}