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

    private var fov = 90.0f
    private val cam = Camera(Math.toRadians(fov.toDouble()).toFloat(), 16f / 9f, 1e-3f, 100.0f)

    private var f3 = true

    init {
        ShaderManager.load("shader").uniform.locate("model").locate("view").locate("projection").locate("tex0")

        glfwSetInputMode(Game.WINDOW_POINTER, GLFW_CURSOR, GLFW_CURSOR_DISABLED)

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
//        glfwSetWindowTitle(Game.WINDOW_POINTER, "FPS: ${1.0 / Game.DELTA_TIME}")

        if (Input.keyStates[GLFW_KEY_ESCAPE] == Input.State.DOWN)
            glfwSetWindowShouldClose(Game.WINDOW_POINTER, true)

        if (Input.keyStates[GLFW_KEY_F1] == Input.State.DOWN)
            glfwSetInputMode(
                Game.WINDOW_POINTER,
                GLFW_CURSOR,
                if (glfwGetInputMode(
                        Game.WINDOW_POINTER,
                        GLFW_CURSOR
                    ) == GLFW_CURSOR_NORMAL
                ) GLFW_CURSOR_DISABLED else GLFW_CURSOR_NORMAL
            )

        if (Input.keyStates[GLFW_KEY_F2] == Input.State.DOWN)
            glPolygonMode(
                GL_FRONT_AND_BACK, when (glGetInteger(GL_POLYGON_MODE)) {
                    GL_FILL -> GL_LINE
                    GL_LINE -> GL_POINT
                    else -> GL_FILL
                }
            )

        if (Input.keyStates[GLFW_KEY_F3] == Input.State.DOWN)
            f3 = !f3

        //Camera Rotation
        if (glfwGetInputMode(Game.WINDOW_POINTER, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
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
            var speed = 3f * Game.DELTA_TIME
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

        //Fov
        run {
            fov -= Input.scrollDelta.y * 5
            fov = fov.coerceIn(30f, 150f)
            cam.fov = Math.toRadians(fov.toDouble()).toFloat()
        }
    }

    override fun render() {
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

        NVGManager.draw().
            color(Color(255,0,255,255)).
            rect(250f,250f,100f,100f).
            draw(false)

        val fps = 1.0f / Game.DELTA_TIME
        val text = String.format("FPS=%.1f\nglfwGetTime=%.2fs", fps, glfwGetTime())
        NVGManager.draw().
            color(Color(255,0,255,255)).
            fontFace("cascadia_code").
            fontSize(32.0f).
            textAlign(NVGDrawCall.TextAlign.LEFT).
            textBox(10.0f, 40.0f, 1920f, text).
            draw(true)
    }

    override fun renderNK() {
//        val rect = NkRect.create()
//        nk_rect(50f, 50f, 220f, 220f, rect)
//
//        val begin: Boolean = nk_begin(
//            NKManager.ctx, "Show", rect,
//            NK_WINDOW_BORDER or NK_WINDOW_MOVABLE or NK_WINDOW_CLOSABLE
//        )
//
//        if (!begin)
//            return
//
//        nk_layout_row_static(NKManager.ctx, 30f, 80, 1)
//        if(nk_button_label(NKManager.ctx, "button")) {
//            log.debug { "Hello" }
//        }
//        nk_layout_row_end(NKManager.ctx)
//
//        nk_end(NKManager.ctx)
    }

}