package com.nami.client

import com.nami.client.imgui.ImGUIManager
import com.nami.client.input.Keyboard
import com.nami.client.input.Mouse
import com.nami.client.resources.Resources
import com.nami.client.scene.SceneManager
import com.nami.client.scene.scenes.LoadingScene
import com.nami.client.scene.scenes.MainMenuScene
import com.nami.client.scene.scenes.SelectWorldScene
import mu.KotlinLogging
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_MULTISAMPLE

class Client {

    companion object {
        var DELTA_TIME = 0F
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

        val loadingScene = LoadingScene({
            val errorCount = Resources.load()
            if (errorCount != 0)
                log.warn { "Completed loading with $errorCount errors" }
            else
                log.info { "Completed loading with 0 errors" }
        }, MainMenuScene())
        SceneManager.set(loadingScene)

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

            Keyboard.update()
            Mouse.update()

            SceneManager.update()

            render()

            Mouse.endFrame()
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