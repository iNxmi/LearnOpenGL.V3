package com.nami.scene.scenes

import com.nami.Window
import com.nami.scene.Scene
import com.nami.scene.SceneManager
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose

class MainMenuScene : Scene() {
    override fun onUpdate() {

    }

    override fun onRender() {

    }

    override fun onRenderHUD() {
        ImGui.setNextWindowPos(0f, 0f)
        ImGui.setNextWindowSize(1920f, 1080f)

        ImGui.getFont().scale = 2.5f
        ImGui.begin("Main Menu", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

        if(ImGui.button("Play"))
            SceneManager.selected = PlayScene()

        if(ImGui.button("Quit"))
            glfwSetWindowShouldClose(Window.pointer, true)

        ImGui.end()
    }

}