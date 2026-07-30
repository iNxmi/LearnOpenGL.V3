package com.nami

import com.jogamp.opengl.GL.GL_MULTISAMPLE
import com.nami.engine.GLFWPlatform
import com.nami.engine.Platform
import com.nami.engine.callbacks.WindowResizeCallback
import com.nami.engine.window.Size
import com.nami.engine.window.Window
import com.nami.legacy.Input
import com.nami.resources.Resources
import com.nami.legacy.scene.SceneManager
import com.nami.scene.scenes.PlayScene
import com.nami.world.World
import mu.KotlinLogging
import org.joml.Vector3i
import org.lwjgl.Version
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

class Game {

    companion object {
        var DELTA_TIME = 0F
    }

    private val log = KotlinLogging.logger {}

    private val platform: Platform = GLFWPlatform()
    private val window: Window

    init {
        log.info("LWJGL Version: ${Version.getVersion()}")
        log.info("Platform API Version: ${platform.version}")

        platform.initialize()

        window = platform.createWindow()
        window.initialize()
        window.size = Size(1920, 1080)
        window.title = "LearnOpengl.V3"
        window.setKeyCallback(Input)
        window.setMouseButtonCallback(Input)
        window.setCursorPositionCallback(Input)
        window.setScrollCallback(Input)
        window.setWindowResizeCallback(object : WindowResizeCallback {
            override fun onWindowResizeCallback(window: Window, width: Int, height: Int) {
                glViewport(0, 0, width, height)
            }
        })

        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_MULTISAMPLE)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        val errorCount = Resources.load()
        if (errorCount != 0) {
            log.warn { "Completed loading with $errorCount errors" }
        } else {
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
            window.poll()

            DELTA_TIME = platform.timeInSeconds.toFloat() - lastTime
            lastTime = platform.timeInSeconds.toFloat()

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

        window.swap()
    }

}