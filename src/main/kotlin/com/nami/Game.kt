package com.nami

import com.jogamp.opengl.GL.GL_MULTISAMPLE
import com.nami.engine.hardware.input.Key
import com.nami.engine.hardware.window.GLFWWindow
import com.nami.engine.hardware.window.Window
import com.nami.legacy.Input
import com.nami.resources.Resources
import com.nami.legacy.scene.SceneManager
import com.nami.scene.scenes.PlayScene
import com.nami.world.World
import mu.KotlinLogging
import org.joml.Vector3i
import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

class Game {

    companion object {
        var DELTA_TIME = 0F
    }

    private val log = KotlinLogging.logger {}

    val window: Window = GLFWWindow()

    init {
        log.info("LWJGL Version: ${Version.getVersion()}")
        log.info("GLFW Version: ${glfwGetVersionString()}")

        window.initialize(1920, 1080, "LearnOpengl.V3")
        window.setKeyCallback(Input)
        window.setMouseButtonCallback(Input)
        window.setCursorPositionCallback(Input)
        window.setScrollCallback(Input)

        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_MULTISAMPLE)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        val errorCount = Resources.load()
        if (errorCount != 0) {
            log.warn { "Completed loading with $errorCount errors" }
        }else {
            log.info { "Completed loading with 0 errors" }
        }

        val world = World(Vector3i(8), System.currentTimeMillis())
        SceneManager.set(PlayScene(window, world))

        window.isVisible = true

        loop()

        window.destroy()
    }

    private fun loop() {
        var lastTime = 0f
        while (!window.shouldClose) {
            glfwPollEvents()

            DELTA_TIME = glfwGetTime().toFloat() - lastTime
            lastTime = glfwGetTime().toFloat()

            SceneManager.update()
            render()
            Input.endFrame()
        }
    }

    private fun render() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

        SceneManager.render()

        val error = glGetError()
        if (error != 0)
            log.warn { "OpenGL Error: $error" }

        window.update()
    }

}