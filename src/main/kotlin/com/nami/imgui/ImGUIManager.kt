package com.nami.imgui

import com.nami.Game
import imgui.ImGui
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import imgui.internal.ImGuiContext
import mu.KotlinLogging
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwGetMonitorContentScale
import org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor

class ImGUIManager {

    companion object {

        private val log = KotlinLogging.logger { }

        @JvmStatic
        var ctx: ImGuiContext? = null

        private val glfw: ImGuiImplGlfw = ImGuiImplGlfw()
        private val gl3: ImGuiImplGl3 = ImGuiImplGl3()

        @JvmStatic
        fun init() {
            ctx = ImGui.createContext()
            ImGui.setCurrentContext(ctx)
            glfw.init(Game.WINDOW_PTR, true)
            gl3.init("#version 330 core")

//            ImGui.getStyle().scaleAllSizes(3.5f)
        }

        @JvmStatic
        fun newFrame() {
            glfw.newFrame()
            ImGui.newFrame()
        }

        @JvmStatic
        fun render() {
            ImGui.render()
            gl3.renderDrawData(ImGui.getDrawData())
        }

        @JvmStatic
        fun delete() {
            gl3.dispose()
            glfw.dispose()
            ImGui.destroyContext()
        }

    }

}