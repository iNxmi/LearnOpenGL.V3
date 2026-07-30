package com.nami.scene.scenes

import com.nami.engine.hardware.input.Key
import com.nami.legacy.Input
import com.nami.engine.hardware.window.Window
import com.nami.resources.GamePath
import com.nami.scene.Scene
import com.nami.world.World
import imgui.type.ImBoolean
import imgui.type.ImInt
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL33.*
import java.awt.image.BufferedImage
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO

class PlayScene(val window: Window, val world: World) : Scene() {

    private val log = KotlinLogging.logger { }

    private var polygonMode = GL_FILL

    private val languageID = ImInt()

    override fun onEnable() {
//        glfwSetInputMode(Window.pointer, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
//
//        if (glfwRawMouseMotionSupported())
//            glfwSetInputMode(Window.pointer, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)
    }

    override fun onUpdate() {
        if (Input.isKeyPressed(Key.KEY_F2)) {
            val width = window.size.width
            val height = window.size.height

            val buffer = BufferUtils.createByteBuffer(width * height * 3)
            glReadPixels(0, 0, width, height, GL_RGB, GL_UNSIGNED_BYTE, buffer)

            GlobalScope.launch {
                val pixels = IntArray(width * height)
                for (i in pixels.indices) {
                    val red = buffer.get().toInt() and 0xFF shl 16
                    val green = buffer.get().toInt() and 0xFF shl 8
                    val blue = buffer.get().toInt() and 0xFF

                    pixels[i] = red or green or blue
                }

                val flipped = IntArray(width * height)
                for (y in 0 until height)
                    if (width >= 0)
                        System.arraycopy(pixels, ((height - 1) - y) * width, flipped, y * width, width)

                val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
                image.setRGB(0, 0, width, height, flipped, 0, width)

                val path =
                    GamePath.screenshots.resolve("${SimpleDateFormat("yyyy_MM_dd__HH_mm_ss_SSS").format(Date())}.png")
                ImageIO.write(image, "png", path.toFile())

                log.info { "Screenshot saved at '$path'" }
            }
        }

        world.update()
    }

    override fun onRender() {
        glPolygonMode(GL_FRONT_AND_BACK, polygonMode)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)

        world.render()
    }

    private val comboPolyMode = ImInt()
    private val fovSlider = floatArrayOf(90.0f)
    private val timeScale = floatArrayOf(1.0f)
    private val fullscreen = ImBoolean(false)

}