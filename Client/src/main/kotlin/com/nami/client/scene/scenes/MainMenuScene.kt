package com.nami.client.scene.scenes

import com.nami.client.Window
import com.nami.client.scene.Scene
import com.nami.client.scene.SceneManager
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose

class MainMenuScene : Scene {

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
        ImGui.begin("Main Menu", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

        if (ImGui.button("Singleplayer"))
            SceneManager.set(SelectWorldScene())

        if (ImGui.button("Multiplayer"))
            SceneManager.set(SelectServerScene())

        if (ImGui.button("Quit"))
            glfwSetWindowShouldClose(Window.pointer, true)

        ImGui.end()
    }

    override fun disable() {

    }

}