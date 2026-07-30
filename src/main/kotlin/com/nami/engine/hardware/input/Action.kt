package com.nami.engine.hardware.input

import org.lwjgl.glfw.GLFW

enum class Action(
    glfwCode: Int
) {

    PRESS(GLFW.GLFW_PRESS),
    RELEASE(GLFW.GLFW_RELEASE),
    REPEAT(GLFW.GLFW_REPEAT);

    object Mapper {
        fun getByGLFW(code: Int) = when (code) {
            GLFW.GLFW_PRESS -> PRESS
            GLFW.GLFW_REPEAT -> REPEAT
            GLFW.GLFW_RELEASE -> RELEASE
            else -> throw IllegalArgumentException()
        }
    }

}