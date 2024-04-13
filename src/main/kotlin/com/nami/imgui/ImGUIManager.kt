package com.nami.imgui

import com.nami.constants.GamePaths
import com.nami.Window
import imgui.ImFont
import imgui.ImFontConfig
import imgui.ImGui
import imgui.gl3.ImGuiImplGl3
import imgui.glfw.ImGuiImplGlfw
import imgui.internal.ImGuiContext
import mu.KotlinLogging
import kotlin.io.path.pathString

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

        private fun loadFont(name: String) {
            val path = GamePaths.fonts.resolve("$name.ttf")

            val config = ImFontConfig()
            config.oversampleH =1
            config.oversampleV = 1
            fonts[name] = ImGui.getIO().fonts.addFontFromFileTTF(path.pathString, 40f, config)

            ImGui.getIO().fonts.build()
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