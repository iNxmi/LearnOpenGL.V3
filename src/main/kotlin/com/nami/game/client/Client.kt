package com.nami.game.client

import com.nami.engine.GLFWPlatform
import com.nami.engine.graphics.Area
import com.nami.engine.graphics.Graphics
import com.nami.engine.graphics.OpenGLGraphics
import com.nami.engine.platform.Platform
import com.nami.engine.platform.callbacks.WindowResizeCallback
import com.nami.engine.platform.window.Size
import com.nami.engine.platform.window.Window
import com.nami.legacy.Input
import com.nami.legacy.scene.SceneManager
import com.nami.resources.Resources
import com.nami.scene.scenes.PlayScene
import com.nami.world.World
import mu.KotlinLogging
import org.joml.Vector3i
import org.lwjgl.Version

class Client {

    companion object {
        var DELTA_TIME = 0F
    }

    private val logger = KotlinLogging.logger {}

    private val platform: Platform = GLFWPlatform()
    private val window: Window

    private val graphics: Graphics = OpenGLGraphics()

    init {
        logger.info("LWJGL Version: ${Version.getVersion()}")

        platform.initialize()

        window = platform.createWindow()
        window.initialize()

        graphics.initialize()

        window.size = Size(1920, 1080)
        window.title = "LearnOpengl.V3"
        window.setKeyCallback(Input)
        window.setMouseButtonCallback(Input)
        window.setCursorPositionCallback(Input)
        window.setScrollCallback(Input)
        window.setWindowResizeCallback(object : WindowResizeCallback {
            override fun onWindowResizeCallback(window: Window, width: Int, height: Int) {
                graphics.viewport = Area(0, 0, width, height)
            }
        })

        val errorCount = Resources.load()
        if (errorCount != 0) {
            logger.warn { "Completed loading with $errorCount errors" }
        } else {
            logger.info { "Completed loading with 0 errors" }
        }

        val world = World(Vector3i(8), System.currentTimeMillis())
        SceneManager.scene = PlayScene(window, world)

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
        graphics.clear()

        SceneManager.render()

        val error = graphics.getErrorCode()
        if (error != 0)
            logger.warn { "Graphics Error Code: $error" }

        window.swap()
    }

}