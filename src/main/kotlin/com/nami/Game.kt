package com.nami

import com.nami.imgui.ImGUIManager
import com.nami.input.Input
import com.nami.resources.Resources
import com.nami.scene.SceneManager
import com.nami.scene.scenes.LoadingScene
import com.nami.scene.scenes.MainMenuScene
import mu.KotlinLogging
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_MULTISAMPLE
import java.util.concurrent.ConcurrentLinkedQueue

class Game {

    companion object {
        var DELTA_TIME = 0F

        val QUEUE = ConcurrentLinkedQueue<Runnable>()

        @JvmStatic
        fun runLater(runnable: Runnable) {
            QUEUE.add(runnable)
        }
    }

    private val log = KotlinLogging.logger {}

    init {
        log.info("LWJGL Version: ${Version.getVersion()}")
        log.info("GLFW Version: ${glfwGetVersionString()}")

        Window.init(1920, 1080)

        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_MULTISAMPLE)

        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        SceneManager.selected = LoadingScene({
            var errorCount = 0
            errorCount += Resources.SHADER.load()
            errorCount += Resources.TEXTURE.load()
            errorCount += Resources.ITEM.load()
            errorCount += Resources.BLOCK.load()
            errorCount += Resources.FEATURE.load()
            errorCount += Resources.BIOME.load()
            errorCount += Resources.MODEL.load()
            errorCount += Resources.PARTICLE.load()
            errorCount += Resources.RECIPE.load()
            errorCount += Resources.LANGUAGE.load()

            if (errorCount != 0)
                log.warn { "Completed loading with $errorCount errors" }
            else
                log.info { "Completed loading with $errorCount errors" }
        }, MainMenuScene())

        glfwShowWindow(Window.pointer)

        loop()

        ImGUIManager.delete()

        glfwFreeCallbacks(Window.pointer)
        glfwDestroyWindow(Window.pointer)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun loop() {
        var lastTime = 0f
        while (!glfwWindowShouldClose(Window.pointer)) {
            DELTA_TIME = glfwGetTime().toFloat() - lastTime
            lastTime = glfwGetTime().toFloat()

            if (QUEUE.isNotEmpty())
                QUEUE.remove().run()

            Input.update()

            SceneManager.update()

            render()

            Input.endFrame()
            glfwPollEvents()
        }
    }

    private fun render() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

        SceneManager.render()
        SceneManager.renderHUD()

        val error = glGetError()
        if (error != 0)
            log.warn { "OpenGL Error: $error" }

        glfwSwapBuffers(Window.pointer)
    }

}

fun main() {
    Game()
}