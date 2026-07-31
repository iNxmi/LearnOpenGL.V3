package com.nami.legacy

import com.nami.engine.GLFWPlatform
import com.nami.engine.platform.Platform
import com.nami.engine.platform.callbacks.WindowResizeCallback
import com.nami.engine.platform.window.Size
import com.nami.engine.platform.window.Window
import com.nami.legacy.scene.SceneManager
import com.nami.resources.Resources
import com.nami.scene.scenes.PlayScene
import com.nami.world.World
import mu.KotlinLogging
import org.joml.Vector3i
import org.lwjgl.Version
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11

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
                GL11.glViewport(0, 0, width, height)
            }
        })

        GL.createCapabilities()
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(com.jogamp.opengl.GL.GL_MULTISAMPLE)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

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
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)

        SceneManager.render()

        val error = GL11.glGetError()
        if (error != 0)
            log.warn { "OpenGL Error: $error" }

        window.swap()
    }

}