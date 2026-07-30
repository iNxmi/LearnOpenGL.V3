package com.nami.engine.hardware.input

import org.lwjgl.glfw.GLFW.*

enum class MouseButton(glfwCode: Int) {

    LEFT(GLFW_MOUSE_BUTTON_LEFT),
    MIDDLE(GLFW_MOUSE_BUTTON_MIDDLE),
    RIGHT(GLFW_MOUSE_BUTTON_RIGHT),

    BUTTON_4(GLFW_MOUSE_BUTTON_4),
    BUTTON_5(GLFW_MOUSE_BUTTON_5),
    BUTTON_6(GLFW_MOUSE_BUTTON_6),
    BUTTON_7(GLFW_MOUSE_BUTTON_7),
    BUTTON_8(GLFW_MOUSE_BUTTON_8), ;

    object Mapper {
        fun getByGLFW(code: Int) = when (code) {
            GLFW_MOUSE_BUTTON_LEFT -> LEFT
            GLFW_MOUSE_BUTTON_MIDDLE -> MIDDLE
            GLFW_MOUSE_BUTTON_RIGHT -> RIGHT
            GLFW_MOUSE_BUTTON_4 -> BUTTON_4
            GLFW_MOUSE_BUTTON_5 -> BUTTON_5
            GLFW_MOUSE_BUTTON_6 -> BUTTON_6
            GLFW_MOUSE_BUTTON_7 -> BUTTON_7
            GLFW_MOUSE_BUTTON_8 -> BUTTON_8
            else -> throw IllegalArgumentException()
        }
    }

}