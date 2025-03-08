package com.nami.scene.scenes

import com.nami.scene.Scene
import com.nami.scene.SceneManager
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.opengl.GL11.glClearColor
import kotlin.math.sin

class LoadingScene(
    private val task: () -> Unit,
    private val nextScene: Scene
) : Scene {

    override fun enable() {
        task.invoke()
        SceneManager.set(nextScene)
    }

    override fun update() {
        val t: Float = (glfwGetTime().toFloat() * 3.14f) / 3f
        val brightness = (sin(t) * 0.5f + 0.5f) * 0.5f

        val colorA = Vector3f(1f, 0f, 0f).mul(brightness)
        val colorB = Vector3f(0f, 0f, 0.5f).mul(0.5f - brightness)

        val color = Vector3f(colorA).add(colorB)
        glClearColor(color.x, color.y, color.z, 1f)
    }

    override fun render() {

    }

    override fun renderHUD() {
        ImGui.setNextWindowSize(1920f, 1080f)
        ImGui.setNextWindowPos(0f, 0f)
        ImGui.setNextWindowBgAlpha(0.0f)

        ImGui.getFont().scale = 5f
        ImGui.begin("Loading", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

        val n = (glfwGetTime() % 3f).toInt() + 1
        val text = "Loading${(".").repeat(n)}"
        val textSize = ImGui.calcTextSize(text)

        val windowSize = ImGui.getWindowSize()

        ImGui.setCursorPos(windowSize.x / 2f - textSize.x / 2f, windowSize.y / 2f - textSize.y / 2f)
        ImGui.text(text)

        ImGui.end()
    }

    override fun disable() {

    }

}