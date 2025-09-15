package com.nami

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil

class Window {

    companion object {
        var pointer: Long = 0L

        var width: Int = 0
        var height: Int = 0

        fun init(width: Int, height: Int) {
            this.width = width
            this.height = height

            GLFWErrorCallback.createPrint(System.err).set()

            if (!glfwInit())
                throw IllegalStateException("Unable to initialize GLFW")

            glfwDefaultWindowHints()
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
            glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE)
            glfwWindowHint(GLFW_FOCUS_ON_SHOW, GLFW_TRUE)
            glfwWindowHint(GLFW_SAMPLES, 16)

            pointer = glfwCreateWindow(width, height, "LearnOpenGL.V3", MemoryUtil.NULL, MemoryUtil.NULL)
            if (pointer == MemoryUtil.NULL)
                throw RuntimeException("Failed to create GLFW Window")

            val videoMode: GLFWVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
            glfwSetWindowPos(pointer, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2)

            glfwSetFramebufferSizeCallback(pointer) { _, width, height ->
                this.width = width
                this.height = height

                GL11.glViewport(0, 0, width, height)
            }

            glfwSetKeyCallback(pointer, Input::onKeyCallback)
            glfwSetMouseButtonCallback(pointer, Input::onMouseButtonCallback)
            glfwSetCursorPosCallback(pointer, Input::onCursorPosCallback)
            glfwSetScrollCallback(pointer, Input::onScrollCallback)

            glfwMakeContextCurrent(pointer)

            glfwSwapInterval(0)
        }

    }

}