package com.nami

import com.nami.nanovg.NVGManager
import com.nami.input.Input
import com.nami.nuklear.NKManager
import com.nami.scene.SceneManager
import com.nami.scene.scenes.TestScene
import mu.KotlinLogging
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_MULTISAMPLE
import org.lwjgl.system.MemoryUtil.NULL

class Game {

    companion object {
        var FPS = 120F
            set(value) {
                FRAME_TIME = 1F / value
                field = value
            }

        var FRAME_TIME = 1F / FPS

        var WINDOW_POINTER = 0L

        var DELTA_TIME = 0F
    }

    private val log = KotlinLogging.logger {}

    init {
        log.info("LWJGL Version: ${Version.getVersion()}")
        log.info("GLFW Version: ${glfwGetVersionString()}")

        //Init GLFW and Create Window
        run {
            GLFWErrorCallback.createPrint(System.err).set()

            if (!glfwInit())
                throw IllegalStateException("Unable to initialize GLFW")

            glfwDefaultWindowHints()
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
            glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
            glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE)
            glfwWindowHint(GLFW_FLOATING, GLFW_TRUE)
            glfwWindowHint(GLFW_FOCUS_ON_SHOW, GLFW_TRUE)
            glfwWindowHint(GLFW_SAMPLES, 8)

            WINDOW_POINTER = glfwCreateWindow(1920, 1080, "Game", NULL, NULL)
            if (WINDOW_POINTER == NULL)
                throw RuntimeException("Failed to create GLFW Window")

            glfwSetKeyCallback(WINDOW_POINTER, Input::onKeyCallback)
            glfwSetMouseButtonCallback(WINDOW_POINTER, Input::onMouseButtonCallback)
            glfwSetCursorPosCallback(WINDOW_POINTER, Input::onCursorPosCallback)
            glfwSetScrollCallback(WINDOW_POINTER, Input::onScrollCallback)

            glfwMakeContextCurrent(WINDOW_POINTER)
            glfwSwapInterval(0)
        }

        GL.createCapabilities()

        NVGManager.init()
        NVGManager.loadFont("roboto")
        NVGManager.loadFont("cascadia_code")

        NKManager.init()

        SceneManager.selected = TestScene()

        glfwShowWindow(WINDOW_POINTER)

        var lastTime = 0.0
        while (!glfwWindowShouldClose(WINDOW_POINTER)) {
            val delta = glfwGetTime() - lastTime
            if (delta < FRAME_TIME)
                continue

            DELTA_TIME = delta.toFloat()

            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

            glDisable(GL_CULL_FACE)
            glCullFace(GL_BACK)
            glFrontFace(GL_CCW)

            glDisable(GL_BLEND)

            glEnable(GL_DEPTH_TEST)
            glEnable(GL_MULTISAMPLE)
            glEnable(GL_STENCIL_TEST)
            glEnable(GL_SCISSOR_TEST)

            glColorMask(true, true, true, true)

            glStencilMask(Integer.MAX_VALUE)
            glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP)
            glStencilFunc(GL_ALWAYS, 0, Integer.MAX_VALUE)

            SceneManager.update()
            SceneManager.render()

            //SceneManager.renderNVG()
            run {
                val width = intArrayOf(1)
                val height = intArrayOf(1)
                glfwGetWindowSize(WINDOW_POINTER, width, height)

                NVGManager.beginFrame(width[0].toFloat(), height[0].toFloat(), 1f)
                SceneManager.renderNVG()
                NVGManager.endFrame()
            }

            SceneManager.renderNK()

            val error = glGetError()
            if(error != 0)
                log.warn { "OpenGL Error: $error" }

            glfwSwapBuffers(WINDOW_POINTER)

            glfwPollEvents()
            Input.update()

            lastTime = glfwGetTime()
        }

        GLMemoryUtils.delete()

        NVGManager.delete()

        glfwFreeCallbacks(WINDOW_POINTER)
        glfwDestroyWindow(WINDOW_POINTER)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

}

fun main() {
    Game()
}