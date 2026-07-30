package com.nami.engine.hardware.window

import com.nami.engine.hardware.callbacks.CursorPositionCallback
import com.nami.engine.hardware.callbacks.KeyCallback
import com.nami.engine.hardware.callbacks.MouseButtonCallback
import com.nami.engine.hardware.callbacks.ScrollCallback
import com.nami.engine.hardware.input.Action
import com.nami.engine.hardware.input.Key
import com.nami.engine.hardware.input.MouseButton
import org.joml.Vector2i
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL11.glViewport
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil

class GLFWWindow : Window {

    private var handle: Long = 0

    override var size: Size
        get() = stackPush().use{stack ->
            val bufferWidth = stack.mallocInt(1)
            val bufferHeight = stack.mallocInt(1)
            glfwGetWindowSize(handle, bufferWidth, bufferHeight)

            val width = bufferWidth[0]
            val height = bufferHeight[0]

            Size(width, height)
        }
        set(value) {
            val width = value.width
            val height = value.height

            glfwSetWindowSize(handle, width, height)
            glViewport(0, 0, width, height)
        }

    override var title: String
        get() = glfwGetWindowTitle(handle)?: ""
        set(value) = glfwSetWindowTitle(handle, value)

    override var shouldClose
        get() = glfwWindowShouldClose(handle)
        set(value) = glfwSetWindowShouldClose(handle, value)

    override var isVisible
        get() = glfwGetWindowAttrib(handle, GLFW_VISIBLE) == GLFW_TRUE
        set(value) = if (value) {
            glfwShowWindow(handle)
        } else {
            glfwHideWindow(handle)
        }

    override val isRawMouseMotionSupported
        get() = glfwRawMouseMotionSupported()

    override fun initialize(width: Int, height: Int, title: String) {
        GLFWErrorCallback.createPrint(System.err).set()

        glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11) // todo temp fix for wayland issue
        if (!glfwInit())
            throw IllegalStateException("Failed to initialize ")

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE)
        glfwWindowHint(GLFW_FOCUS_ON_SHOW, GLFW_TRUE)
        glfwWindowHint(GLFW_SAMPLES, 4)

        handle = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (handle == MemoryUtil.NULL)
            throw RuntimeException("Failed to create Window.")

        val videoMode: GLFWVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
        glfwSetWindowPos(handle, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2)

        glfwSetFramebufferSizeCallback(handle) { _, width, height -> size = Size(width, height) }

        makeContextCurrent()

        glfwSwapInterval(0)
    }

    override fun update() = glfwSwapBuffers(handle)

    override fun destroy() {
        glfwFreeCallbacks(handle)
        glfwDestroyWindow(handle)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    override fun makeContextCurrent() = glfwMakeContextCurrent(handle)

    override fun setKeyCallback(callback: KeyCallback) {
        glfwSetKeyCallback(handle, fun(_, keyCode: Int, _, actionCode: Int, _) {
            val key = Key.Mapper.getByGLFW(keyCode)
            val action = Action.Mapper.getByGLFW(actionCode)
            callback.onKeyCallback(this, key, action)
        })
    }

    override fun setMouseButtonCallback(callback: MouseButtonCallback) {
        glfwSetMouseButtonCallback(handle, fun(_, buttonCode: Int, actionCode: Int, _) {
            val button = MouseButton.Mapper.getByGLFW(buttonCode)
            val action = Action.Mapper.getByGLFW(actionCode)
            callback.onMouseButtonCallback(this, button, action)
        })
    }

    override fun setCursorPositionCallback(callback: CursorPositionCallback) {
        glfwSetCursorPosCallback(handle, fun(_, x: Double, y: Double) {
            callback.onCursorPositionCallback(this, x.toInt(), y.toInt())
        })
    }

    override fun setScrollCallback(callback: ScrollCallback) {
        glfwSetScrollCallback(handle, fun(_, x: Double, y: Double) {
            callback.onScrollCallback(this, x.toInt(), y.toInt())
        })
    }

}