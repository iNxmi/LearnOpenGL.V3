package com.nami

import com.nami.imgui.ImGUIManager
import com.nami.nanovg.NVGManager
import com.nami.input.Input
import com.nami.scene.SceneManager
import com.nami.scene.scenes.MaxwellScene
import com.nami.scene.scenes.TestScene
import mu.KotlinLogging
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.glfw.GLFWWindowSizeCallbackI
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_MULTISAMPLE
import org.lwjgl.system.MemoryUtil.NULL
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.*

class Game {

    companion object {

        var V_SYNC = false

        var FPS = 120F
            set(value) {
                FRAME_TIME = 1F / value
                field = value
            }

        var FRAME_TIME = 1F / FPS

        var WINDOW_PTR = 0L

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
            glfwWindowHint(GLFW_FOCUS_ON_SHOW, GLFW_TRUE)
            glfwWindowHint(GLFW_SAMPLES, 16)

            WINDOW_PTR = glfwCreateWindow(1920, 1080, "LearnOpenGL.V3", NULL, NULL)
            if (WINDOW_PTR == NULL)
                throw RuntimeException("Failed to create GLFW Window")

            val videoMode: GLFWVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!
            glfwSetWindowPos(WINDOW_PTR, (videoMode.width() - 1920) / 2, (videoMode.height() - 1080) / 2)

            glfwSetFramebufferSizeCallback(WINDOW_PTR) { _, width, height ->
                glViewport(0, 0, width, height)
            }

            glfwSetKeyCallback(WINDOW_PTR, Input::onKeyCallback)
            glfwSetMouseButtonCallback(WINDOW_PTR, Input::onMouseButtonCallback)
            glfwSetCursorPosCallback(WINDOW_PTR, Input::onCursorPosCallback)
            glfwSetScrollCallback(WINDOW_PTR, Input::onScrollCallback)

            glfwMakeContextCurrent(WINDOW_PTR)
            glfwSwapInterval(if (V_SYNC) 1 else 0)
        }

        GL.createCapabilities()

        NVGManager.init()
        NVGManager.loadFont("roboto")
        NVGManager.loadFont("cascadia_code")

        ImGUIManager.init()

        SceneManager.selected = TestScene()

        glfwShowWindow(WINDOW_PTR)

        var lastTime = 0.0
        while (!glfwWindowShouldClose(WINDOW_PTR)) {
            glfwPollEvents()

            val delta = glfwGetTime() - lastTime
            if (delta < FRAME_TIME)
                continue

            Input.update()

            DELTA_TIME = delta.toFloat()

            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

            glEnable(GL_CULL_FACE)
            glCullFace(GL_BACK)
            glFrontFace(GL_CCW)

            glDisable(GL_BLEND)

            glEnable(GL_DEPTH_TEST)
            glEnable(GL_MULTISAMPLE)
            glEnable(GL_SCISSOR_TEST)

            glColorMask(true, true, true, true)

            glDisable(GL_STENCIL_TEST)
            glStencilMask(Integer.MAX_VALUE)
            glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP)
            glStencilFunc(GL_ALWAYS, 0, Integer.MAX_VALUE)

            SceneManager.render()

            //SceneManager.renderNVG()
            run {
                val width = intArrayOf(1)
                val height = intArrayOf(1)
                glfwGetWindowSize(WINDOW_PTR, width, height)

                NVGManager.beginFrame(width[0].toFloat(), height[0].toFloat(), 1f)
                SceneManager.renderNVG()
                NVGManager.endFrame()
            }

            //SceneManager.renderImGUI()
            run {
                ImGUIManager.newFrame()
                SceneManager.renderImGUI()
                ImGUIManager.render()
            }

            SceneManager.update()

            val error = glGetError()
            if (error != 0)
                log.warn { "OpenGL Error: $error" }

            glfwSwapBuffers(WINDOW_PTR)

            lastTime = glfwGetTime()
        }

        ImGUIManager.delete()
        NVGManager.delete()

        glfwFreeCallbacks(WINDOW_PTR)
        glfwDestroyWindow(WINDOW_PTR)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

}

fun main() {
    Game()
}