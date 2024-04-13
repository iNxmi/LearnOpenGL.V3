package com.nami

import com.nami.imgui.ImGUIManager
import com.nami.input.Input
import com.nami.scene.SceneManager
import com.nami.scene.scenes.MainMenuScene
import mu.KotlinLogging
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_MULTISAMPLE

class Game {

    /*
    * Shadows
    * Particles
    * Audio
    * */

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

//        val device = alcOpenDevice(null as ByteBuffer?)
//        val deviceCaps = ALC.createCapabilities(device)
//
//        val context = alcCreateContext(device, null as IntBuffer?)
//        alcMakeContextCurrent(context)
//        AL.createCapabilities(deviceCaps)

        SceneManager.selected = MainMenuScene()

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

            glfwPollEvents()

            if(DELTA_TIME < 1f/120.0f)
                continue

            lastTime += DELTA_TIME


            Input.update()

            SceneManager.update()

            render()
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