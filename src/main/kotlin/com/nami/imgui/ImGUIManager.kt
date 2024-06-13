package com.nami.imgui

import com.nami.Window
import imgui.ImFont
import imgui.ImGui
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import imgui.internal.ImGuiContext
import mu.KotlinLogging

class ImGUIManager {

    companion object {

        private val log = KotlinLogging.logger { }

        private var init = false

        @JvmStatic
        var ctx: ImGuiContext? = null

        private val glfw: ImGuiImplGlfw = ImGuiImplGlfw()
        private val gl3: ImGuiImplGl3 = ImGuiImplGl3()

        @JvmStatic
        val fonts = mutableMapOf<String, ImFont>()

        private fun init() {
            ctx = ImGui.createContext()
            ImGui.setCurrentContext(ctx)
            glfw.init(Window.pointer, true)
            gl3.init("#version 330 core")

//            loadFont("roboto")
//            loadFont("cascadia_code")

            init = true
        }

        @JvmStatic
        fun newFrame() {
            if (!init)
                init()

            glfw.newFrame()
            ImGui.newFrame()
        }

        @JvmStatic
        fun render() {
            if (!init)
                init()

            ImGui.render()
            gl3.renderDrawData(ImGui.getDrawData())
        }

        @JvmStatic
        fun delete() {
            if (!init)
                return

            gl3.dispose()
            glfw.dispose()
            ImGui.destroyContext()
        }

    }

}