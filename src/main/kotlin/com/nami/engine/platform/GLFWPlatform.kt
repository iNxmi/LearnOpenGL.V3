package com.nami.engine

import com.nami.engine.platform.Platform
import com.nami.engine.platform.window.GLFWWindow
import mu.KotlinLogging
import org.lwjgl.glfw.GLFW.GLFW_PLATFORM
import org.lwjgl.glfw.GLFW.GLFW_PLATFORM_X11
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.glfw.GLFW.glfwGetVersionString
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwInitHint
import org.lwjgl.glfw.GLFW.glfwSwapInterval
import org.lwjgl.glfw.GLFWErrorCallback

class GLFWPlatform: Platform {

    private val logger = KotlinLogging.logger {}

    override val version = glfwGetVersionString()
    override val timeInSeconds
        get() = glfwGetTime()

    override var isVsyncEnabled: Boolean = true
        set(value) {
            val interval = if(value) 1 else 0
            glfwSwapInterval(interval)

            field = value
        }

    override fun initialize() {
        logger.info { "GLFW Version: $version" }

        GLFWErrorCallback.createPrint(System.err).set()

        glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11) // todo temp fix for wayland issue
        if (!glfwInit())
            throw IllegalStateException("Failed to initialize GLFW.")
    }

    override fun createWindow() = GLFWWindow()

}