package com.nami.scene.scenes

import com.nami.Game
import com.nami.constants.GamePaths
import com.nami.Window
import com.nami.input.Input
import com.nami.scene.Scene
import com.nami.scene.SceneManager
import com.nami.world.World
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import imgui.type.ImInt
import mu.KotlinLogging
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL33.*
import java.awt.image.BufferedImage
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import javax.imageio.ImageIO


class PlayScene : Scene() {

    private val log = KotlinLogging.logger { }

    private val world = World()

    private var polygonMode = GL_FILL
    private var cullMode = GL_BACK
    private var cullfaceEnabled = false

    private var menu = false
    private var inventory = false
    private var f3 = true

    override fun onInit() {
        glfwSetInputMode(Window.pointer, GLFW_CURSOR, GLFW_CURSOR_DISABLED)

        if (glfwRawMouseMotionSupported())
            glfwSetInputMode(Window.pointer, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)
    }

    override fun onUpdate() {
        if (Input.keyStates[GLFW_KEY_ESCAPE] == Input.State.DOWN) {
            if (inventory)
                inventory = !inventory
            else
                menu = !menu
        }

        if (Input.keyStates[GLFW_KEY_E] == Input.State.DOWN)
            inventory = !inventory && !menu

        glfwSetInputMode(
            Window.pointer,
            GLFW_CURSOR,
            if (menu || inventory) GLFW_CURSOR_NORMAL else GLFW_CURSOR_DISABLED
        )

//        if (Input.mouseButtonStates[GLFW_MOUSE_BUTTON_LEFT] == Input.State.DOWN) {
//            chunk.blocks[Vector3i().set(
//                player.transform.position.x.toInt(),
//                player.transform.position.y.toInt() - 2,
//                player.transform.position.z.toInt()
//            )] = Vector3f(1f, 0f, 0f)
//        }

        if (Input.keyStates[GLFW_KEY_F2] == Input.State.DOWN) {
            val widthArr = IntArray(1)
            val heightArr = IntArray(1)
            glfwGetWindowSize(Window.pointer, widthArr, heightArr)

            val width = widthArr[0]
            val height = heightArr[0]

            val buffer = BufferUtils.createByteBuffer(width * height * 3)
            glReadPixels(0, 0, width, height, GL_RGB, GL_UNSIGNED_BYTE, buffer)

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

            val path = GamePaths.screenshots.resolve("${SimpleDateFormat("yyyy-MM-dd--HH-mm-ss-SSS").format(Date())}.png")
            ImageIO.write(image, "png", path.toFile())

            log.info { "Screenshot saved at '$path'" }
        }

        world.update(time)
    }

    override fun onRender() {
        glPolygonMode(GL_FRONT_AND_BACK, polygonMode)
        if (cullfaceEnabled) glEnable(GL_CULL_FACE) else glDisable(GL_CULL_FACE)
        glCullFace(cullMode)

        world.render()
    }

    private val comboPolyMode = ImInt()
    private val comboCullMode = ImInt(1)
    private val timeScale = floatArrayOf(1.0f)
    private val worldTime = floatArrayOf(0.0f)

    override fun onRenderHUD() {
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

        if (f3 && !menu) {
            ImGui.setNextWindowPos(0f, 0f)
            ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())
            ImGui.setNextWindowBgAlpha(0.0f)

            ImGui.getFont().scale = 2.5f
            ImGui.begin("HUD", ImBoolean(), ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

            ImGui.text("FPS=${1f / Game.DELTA_TIME}")
            ImGui.text("glfwGetTime()=${glfwGetTime()}")
            ImGui.text("time.time=${time.time}")
            ImGui.text("player.position=${world.player.transform.position.toString(NumberFormat.getNumberInstance())}")

            val duration = java.time.Duration.ofMillis((world.worldTime * world.millisPerDay).toLong())
            ImGui.text(
                "daylightPercentage=${
                    String.format(
                        "%.5f",
                        world.daylightPercentage
                    )
                } (${duration.toHours()}:${duration.toMinutesPart()}:${duration.toSecondsPart()})"
            )
            ImGui.text("sinT=${String.format("%.5f", world.sinT)}")
            ImGui.text("cosT=${String.format("%.5f", world.cosT)}")

            ImGui.end()
        }

        if (inventory) {
            ImGui.setNextWindowPos(0f, 0f)
            ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())
            ImGui.setNextWindowBgAlpha(0.4f)

            ImGui.getFont().scale = 2f
            ImGui.begin("Inventory", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

            ImGui.end()
        }

        if (menu) {
            ImGui.setNextWindowPos(0f, 0f)
            ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())
            ImGui.setNextWindowBgAlpha(0.4f)

            ImGui.getFont().scale = 2f
            ImGui.begin("Settings", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

            if (ImGui.sliderFloat("World Time", worldTime, 0f, 1f))
                world.worldTime = worldTime[0]

            if (ImGui.collapsingHeader("Time")) {
                if (ImGui.sliderFloat("Scale", timeScale, 0f, 5f))
                    time.scale = timeScale[0]
            }

            if (ImGui.collapsingHeader("OpenGL")) {
                if (ImGui.combo("glPolygonMode", comboPolyMode, arrayOf("GL_FILL", "GL_LINE", "GL_POINT")))
                    polygonMode = when (comboPolyMode.get()) {
                        0 -> GL_FILL
                        1 -> GL_LINE
                        2 -> GL_POINT
                        else -> 0
                    }

                if (ImGui.checkbox("glEnable(GL_CULL_FACE)", cullfaceEnabled))
                    cullfaceEnabled = !cullfaceEnabled

                ImGui.sameLine()

                if (ImGui.combo("glCullFace", comboCullMode, arrayOf("GL_FRONT_AND_BACK", "GL_BACK", "GL_FRONT")))
                    cullMode = when (comboCullMode.get()) {
                        0 -> GL_FRONT_AND_BACK
                        1 -> GL_BACK
                        2 -> GL_FRONT
                        else -> 0
                    }
            }

            if (ImGui.radioButton("F3 Overlay", f3))
                f3 = !f3

            if (ImGui.button("Main Menu"))
                SceneManager.selected = MainMenuScene()

            ImGui.end()
        }
    }

}