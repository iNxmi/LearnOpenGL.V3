package com.nami.engine.platform.input

import org.lwjgl.glfw.GLFW.*

enum class CursorMode(val glfwCode: Int) {

    NORMAL(GLFW_CURSOR_NORMAL),
    HIDDEN(GLFW_CURSOR_HIDDEN),
    DISABLED(GLFW_CURSOR_DISABLED),
    CAPTURED(GLFW_CURSOR_CAPTURED);

    object Mapper {
        fun getByGLFW(code: Int) = when (code) {
            GLFW_CURSOR_NORMAL -> NORMAL
            GLFW_CURSOR_HIDDEN -> HIDDEN
            GLFW_CURSOR_DISABLED -> DISABLED
            GLFW_CURSOR_CAPTURED -> CAPTURED
            else -> throw IllegalArgumentException()
        }
    }

}