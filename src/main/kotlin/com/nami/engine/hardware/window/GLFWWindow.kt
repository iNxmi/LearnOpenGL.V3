package com.nami.engine.hardware.window

import com.nami.Input
import com.nami.engine.hardware.callbacks.CursorPositionCallback
import com.nami.engine.hardware.callbacks.KeyCallback
import com.nami.engine.hardware.callbacks.MouseButtonCallback
import com.nami.engine.hardware.callbacks.ScrollCallback
import com.nami.engine.hardware.input.Action
import com.nami.engine.hardware.input.Key
import com.nami.engine.hardware.input.MouseButton
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil

class GLFWWindow(
    override var width: Int,
    override var height: Int,
    override var title: String
) : Window {

    private var pointer: Long = 0

    override fun initialize() {
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit())
            throw IllegalStateException("Failed to initialize ")

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE)
        glfwWindowHint(GLFW_FOCUS_ON_SHOW, GLFW_TRUE)
        glfwWindowHint(GLFW_SAMPLES, 16)

        pointer = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (pointer == MemoryUtil.NULL)
            throw RuntimeException("Failed to create Window.")

        val videoMode: GLFWVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
        glfwSetWindowPos(pointer, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2)

        glfwSetFramebufferSizeCallback(pointer) { _, width, height ->
            this.width = width
            this.height = height

            GL11.glViewport(0, 0, width, height)
        }

        makeContextCurrent()

        glfwSwapInterval(0)
    }

    override fun destroy() {
        glfwFreeCallbacks(pointer)
        glfwDestroyWindow(pointer)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    override fun shouldClose(): Boolean = glfwWindowShouldClose(pointer)

    override fun makeContextCurrent() = glfwMakeContextCurrent(pointer)

    override fun setKeyCallback(callback: KeyCallback) {
        glfwSetKeyCallback(pointer, fun(_, keyCode: Int, _, actionCode: Int, _) {
            val key = Key.Mapper.getByGLFW(keyCode)
            val action = Action.Mapper.getByGLFW(actionCode)
            callback.onKeyCallback(this, key, action)
        })
    }

    override fun setMouseButtonCallback(callback: MouseButtonCallback) {
        glfwSetMouseButtonCallback(pointer, fun(_, buttonCode: Int, actionCode: Int, _) {
            val button = MouseButton.Mapper.getByGLFW(buttonCode)
            val action = Action.Mapper.getByGLFW(actionCode)
            callback.onMouseButtonCallback(this, button, action)
        })
    }

    override fun setCursorPositionCallback(callback: CursorPositionCallback) {
        glfwSetCursorPosCallback(pointer, fun(_, x: Double, y: Double){
            callback.onCursorPositionCallback(this, x.toInt(), y.toInt())
        })
    }

    override fun setScrollCallback(callback: ScrollCallback) {
        glfwSetCursorPosCallback(pointer, fun(_, x: Double, y: Double){
            callback.onScrollCallback(this, x.toInt(), y.toInt())
        })
    }


}