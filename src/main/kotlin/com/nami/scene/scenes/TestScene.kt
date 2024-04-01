package com.nami.scene.scenes

import com.nami.Camera
import com.nami.GLMemoryUtils
import com.nami.Game
import com.nami.constants.Directions
import com.nami.input.Input
import com.nami.nanovg.NVGDrawCall
import com.nami.nanovg.NVGManager
import com.nami.scene.Scene
import com.nami.shader.ShaderManager
import imgui.ImGui
import imgui.type.ImFloat
import imgui.type.ImInt
import mu.KotlinLogging
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL33.*
import java.awt.Color
import java.nio.file.Paths
import javax.imageio.ImageIO
import kotlin.math.cos
import kotlin.math.sin


class TestScene : Scene {

    private val log = KotlinLogging.logger { }

    private val vao = GLMemoryUtils.genVertexArray()
    private val texture = glGenTextures()

    private val fps = ImInt(Game.FPS.toInt())

    private val fov = floatArrayOf(90.0f)
    private val clip = floatArrayOf(1e-3f, 100.0f)
    private val aspect = intArrayOf(16, 9)
    private val cam =
        Camera(Math.toRadians(fov[0].toDouble()).toFloat(), aspect[0].toFloat() / aspect[1].toFloat(), clip[0], clip[1])

    private val camSpeed = floatArrayOf(3f)

    private var polygonMode = GL_FILL

    private var menu = false
    private var f3 = true

    init {
        ShaderManager.load("shader").uniform.locate("model").locate("view").locate("projection").locate("tex0")

        glfwSetInputMode(Game.WINDOW_PTR, GLFW_CURSOR, GLFW_CURSOR_DISABLED)

        //Create Cube VAO
        run {
            glBindVertexArray(vao)

            val vbo = GLMemoryUtils.genBuffer()
            glBindBuffer(GL_ARRAY_BUFFER, vbo)
            val vertices: FloatArray = floatArrayOf(
                0.5f, -0.5f, -0.5f, 1f, 1f,
                0.5f, -0.5f, 0.5f, 1f, 0f,
                -0.5f, -0.5f, 0.5f, 0f, 1f,
                -0.5f, -0.5f, -0.5f, 1f, 0f,
                0.5f, 0.5f, -0.5f, 1f, 1f,
                0.5f, 0.5f, 0.5f, 1f, 0f,
                -0.5f, 0.5f, 0.5f, 0f, 0f,
                -0.5f, 0.5f, -0.5f, 0f, 1f,
            )
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

            glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.SIZE_BYTES, 0)
            glEnableVertexAttribArray(0)

            glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)
            glEnableVertexAttribArray(1)

            val ebo = GLMemoryUtils.genBuffer()
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
            val indices: IntArray = intArrayOf(
                1, 2, 3,
                4, 7, 6,
                4, 5, 1,
                1, 5, 6,
                6, 7, 3,
                4, 0, 3,
                0, 1, 3,
                5, 4, 6,
                0, 4, 1,
                2, 1, 6,
                2, 6, 3,
                7, 4, 3
            )
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

            glBindVertexArray(0)
        }

        //Create Texture
        run {
            val image = ImageIO.read(Paths.get("src", "main", "resources", "textures", "planks.png").toUri().toURL())

            val pixels = IntArray(image.width * image.height)
            image.getRGB(0, 0, image.width, image.height, pixels, 0, image.width)

            val buffer = BufferUtils.createByteBuffer(pixels.size * 4)
            for (y in 0 until image.height)
                for (x in 0 until image.width) {
                    val pixel = pixels[y * image.width + x]

                    buffer.put(((pixel shr 16) and 0xFF).toByte())
                    buffer.put(((pixel shr 8) and 0xFF).toByte())
                    buffer.put((pixel and 0xFF).toByte())
                    buffer.put(((pixel shr 24) and 0xFF).toByte())
                }
            buffer.flip()

            glActiveTexture(GL_TEXTURE0)
            glBindTexture(GL_TEXTURE_2D, texture)

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
            glGenerateMipmap(GL_TEXTURE_2D)

            glBindTexture(GL_TEXTURE_2D, 0)
        }

    }

    private var yaw = 0f
    private var pitch = 0f

    override fun update() {
        if (Input.keyStates[GLFW_KEY_ESCAPE] == Input.State.DOWN) {
            menu = !menu
            glfwSetInputMode(Game.WINDOW_PTR, GLFW_CURSOR, if (menu) GLFW_CURSOR_NORMAL else GLFW_CURSOR_DISABLED)
        }

        //Camera Rotation
        if (glfwGetInputMode(Game.WINDOW_PTR, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
            val sensivity = 0.075f

            yaw += Input.posDelta.x * sensivity
            pitch -= Input.posDelta.y * sensivity
            pitch = pitch.coerceIn(-90f, 90f)

            cam.direction.set(
                cos(Math.toRadians(yaw.toDouble())) * cos(Math.toRadians(pitch.toDouble())),
                sin(Math.toRadians(pitch.toDouble())),
                sin(Math.toRadians(yaw.toDouble())) * cos(Math.toRadians(pitch.toDouble()))
            ).normalize()
        }

        //Camera Movement
        run {
            var speed = camSpeed[0] * Game.DELTA_TIME
            if (Input.isKeyInStates(GLFW_KEY_LEFT_SHIFT, Input.State.DOWN, Input.State.HOLD))
                speed *= 5f

            if (Input.isKeyInStates(GLFW_KEY_W, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(Vector3f(cam.direction).mul(1f, 0f, 1f).mul(-speed))
            if (Input.isKeyInStates(GLFW_KEY_S, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(Vector3f(cam.direction).mul(1f, 0f, 1f).mul(speed))

            if (Input.isKeyInStates(GLFW_KEY_A, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(Vector3f(cam.direction).cross(Directions.UP).normalize().mul(speed))
            if (Input.isKeyInStates(GLFW_KEY_D, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(Vector3f(cam.direction).cross(Directions.UP).normalize().mul(-speed))

            if (Input.isKeyInStates(GLFW_KEY_SPACE, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(0f, -speed, 0f)
            if (Input.isKeyInStates(GLFW_KEY_LEFT_CONTROL, Input.State.DOWN, Input.State.HOLD))
                cam.position.add(0f, speed, 0f)
        }
    }

    override fun render() {
        glPolygonMode(GL_FRONT_AND_BACK, polygonMode)

        val shader = ShaderManager.bind("shader")

        shader.uniform.set("model", Matrix4f()).set("projection", cam.projection).set("view", cam.view).set("tex0", 0)

        glBindTexture(GL_TEXTURE_2D, texture)

        glBindVertexArray(vao)

        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0)

        glBindVertexArray(0)

        glBindTexture(GL_TEXTURE_2D, 0)

        ShaderManager.unbind()
    }

    override fun renderNVG() {
        if (!f3)
            return

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

        val fps = 1.0f / Game.DELTA_TIME
        val text = String.format("FPS=%.1f\nDELTA_TIME=%f\nglfwGetTime=%.2fs", fps, Game.DELTA_TIME, glfwGetTime())
        NVGManager.draw().color(Color(1f, 1f, 1f, 1f)).fontFace("cascadia_code").fontSize(32.0f)
            .textAlign(NVGDrawCall.TextAlign.LEFT).textBox(10.0f, 40.0f, 1920f, text).draw(true)
    }

    val window = ImInt()
    val comboPolyMode = ImInt()
    override fun renderImGUI() {
        if (!menu)
            return

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

        ImGui.begin("Settings")

        if (ImGui.collapsingHeader("Window")) {

//            if (ImGui.checkbox("vSync", Game.V_SYNC)) {
//                Game.V_SYNC = !Game.V_SYNC
//                glfwSwapInterval(if (Game.V_SYNC) 1 else 0)
//            }
//            ImGui.sameLine()
            if (ImGui.inputInt("FPS", fps))
                Game.FPS = fps.toFloat()

//            if (ImGui.combo("Window Mode", window, arrayOf("Windowed", "Windowed Fullscreen", "Fullscreen"))) {
//
//            }
        }

        if (ImGui.collapsingHeader("Camera")) {
            if (ImGui.sliderFloat("FOV", fov, 1f, 179f))
                cam.fov = Math.toRadians(fov[0].toDouble()).toFloat()

            if (ImGui.sliderFloat2("zNear / zFar", clip, 1e-3f, 100.0f)) {
                cam.zNear = clip[0]
                cam.zFar = clip[1]
            }

            if (ImGui.inputInt2("Aspect Ratio", aspect)) {
                aspect[0] = aspect[0].coerceIn(1, 100)
                aspect[1] = aspect[1].coerceIn(1, 100)
                cam.aspect = aspect[0].toFloat() / aspect[1].toFloat()
            }

            ImGui.sliderFloat("Speed", camSpeed, 0.5f, 20.0f)
        }

        if (ImGui.collapsingHeader("OpenGL")) {
            if (ImGui.combo("glPolygonMode", comboPolyMode, arrayOf("GL_FILL", "GL_LINE", "GL_POINT")))
                polygonMode = when (comboPolyMode.get()) {
                    0 -> GL_FILL
                    1 -> GL_LINE
                    2 -> GL_POINT
                    else -> 0
                }
        }

        if (ImGui.radioButton("F3 Overlay", f3))
            f3 = !f3

        if (ImGui.button("Quit"))
            glfwSetWindowShouldClose(Game.WINDOW_PTR, true)

        ImGui.end()
    }

}