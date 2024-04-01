package com.nami.imgui

import com.nami.Game
import imgui.ImGui
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import imgui.internal.ImGuiContext

class ImGUIManager {

    companion object {

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