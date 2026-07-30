package com.nami.scene.scenes

import com.nami.Window
import com.nami.scene.Scene
import com.nami.scene.SceneManager
import com.nami.world.World
import imgui.ImGui
import imgui.flag.ImGuiDataType
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImLong
import org.joml.Vector3i
import org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose

class MainMenuScene : Scene() {

    val seed = ImLong(System.currentTimeMillis())

    override fun onRenderHUD() {
        ImGui.setNextWindowPos(0f, 0f)
        ImGui.setNextWindowSize(1920f, 1080f)

        ImGui.getFont().scale = 2.5f
        ImGui.begin("Main Menu", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

        ImGui.inputScalar("Seed", ImGuiDataType.S64, seed)
        if (ImGui.button("Play")) {
            val world = World(Vector3i(512), seed.get())
            SceneManager.set(PlayScene(world))
        }

        if (ImGui.button("Quit"))
            glfwSetWindowShouldClose(Window.pointer, true)

        ImGui.end()
    }

}