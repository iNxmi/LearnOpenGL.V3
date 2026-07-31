package com.nami.engine.input

import org.lwjgl.glfw.GLFW.*

enum class CursorMode(val glfwCode: Int) {

    NORMAL(GLFW_CURSOR_NORMAL),
    HIDDEN(GLFW_CURSOR_HIDDEN),
    DISABLED(GLFW_CURSOR_DISABLED),
    CAPTURED(GLFW_CURSOR_CAPTURED)

}