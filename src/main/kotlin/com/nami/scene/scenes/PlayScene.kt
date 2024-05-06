package com.nami.scene.scenes

import com.nami.Game
import com.nami.constants.GamePaths
import com.nami.Window
import com.nami.input.Input
import com.nami.scene.Scene
import com.nami.scene.SceneManager
import com.nami.world.chunk.Chunk
import com.nami.world.World
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import imgui.type.ImInt
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.joml.Vector2i
import org.joml.Vector3d
import org.joml.Vector3i
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL33.*
import java.awt.Color
import java.awt.image.BufferedImage
import java.nio.file.Path
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.max


class PlayScene(val path: Path?) : Scene() {

    private val log = KotlinLogging.logger { }

    private val world = World(0)

    private var polygonMode = GL_FILL

    private var menu = false
    private var inventory = false
    private var f3 = true

    override fun onInit() {
        glfwSetInputMode(Window.pointer, GLFW_CURSOR, GLFW_CURSOR_CAPTURED)

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

        if (Input.keyStates[GLFW_KEY_F2] == Input.State.DOWN) {
            val width = Window.width
            val height = Window.height

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
                    GamePaths.screenshots.resolve("${SimpleDateFormat("yyyy-MM-dd--HH-mm-ss-SSS").format(Date())}.png")
                ImageIO.write(image, "png", path.toFile())

                log.info { "Screenshot saved at '$path'" }
            }
        }

        world.update(time)
    }

    override fun onRender() {
        glPolygonMode(GL_FRONT_AND_BACK, polygonMode)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
//        glEnable(GL_FRAMEBUFFER_SRGB)

        world.render(time)
    }

    private val comboPolyMode = ImInt()
    private val timeScale = floatArrayOf(1.0f)
    private val fullscreen = ImBoolean(false)

    override fun onRenderHUD() {
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

        if (f3 && !menu && !inventory) {
            ImGui.setNextWindowPos(0f, 0f)
            ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())
            ImGui.setNextWindowBgAlpha(0.0f)

            ImGui.getFont().scale = 2.5f
            ImGui.begin("HUD", ImBoolean(), ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

            ImGui.text("FPS=${1f / Game.DELTA_TIME}")
//            val biome = world.getBiome(
//                Vector3i(
//                    world.player.transform.position.x.toInt(),
//                    world.player.transform.position.y.toInt(),
//                    world.player.transform.position.z.toInt()
//                )
//            )
            ImGui.text("seed=${world.seed}")
//            ImGui.text("biome=${biome?.template?.name} factors=${biome?.factors}")
            ImGui.text("position=${world.player.transform.position}")
            ImGui.text("block_position=${Vector3i().set(Vector3d(world.player.transform.position))}")

            ImGui.end()
        }

        if (inventory) {
            ImGui.setNextWindowPos(0f, 0f)
            ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())
            ImGui.setNextWindowBgAlpha(0.4f)

            ImGui.getFont().scale = 4f
            ImGui.begin("Inventory", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

            world.player.inventory.map.forEach { (block, count) -> ImGui.text("${block.name}: $count") }

            ImGui.end()
        }

        if (menu) {
            ImGui.setNextWindowPos(0f, 0f)
            ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())
            ImGui.setNextWindowBgAlpha(0.4f)

            ImGui.getFont().scale = 2f
            ImGui.begin("Settings", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

            if (ImGui.collapsingHeader("World")) {
                if (ImGui.button("Capture Map")) {
//                    GlobalScope.launch {
//                        val bounds = Vector2i()
//                        world.chunkManager.chunks.forEach { (position, _) ->
//                            bounds.set(
//                                max(position.x + 1, bounds.x),
//                                max(position.z + 1, bounds.y)
//                            )
//                        }
//
//                        val image =
//                            BufferedImage(bounds.x * Chunk.size.x, bounds.y * Chunk.size.z, BufferedImage.TYPE_INT_RGB)
//                        world.chunkManager.chunks.forEach { (posChunk, chunk) ->
//                            for (x in 0 until Chunk.size.x)
//                                for (y in 0 until Chunk.size.y)
//                                    for (z in 0 until Chunk.size.z) {
//                                        val globalPos = Vector3i(x, y, z).add(
//                                            Vector3i(posChunk).mul(
//                                                Chunk.size.x,
//                                                Chunk.size.y,
//                                                Chunk.size.z
//                                            )
//                                        )
//
//                                        if (globalPos.x < 0 || globalPos.y < 0)
//                                            continue
//
//                                        val block = world.getBlock(
//                                            Vector3i(
//                                                globalPos.x,
//                                                world.getHeight(Vector2i(globalPos.x, globalPos.y), Chunk.size.y + 1),
//                                                globalPos.y
//                                            )
//                                        ) ?: continue
//
//                                        val color = Color(
//                                            (block.color.cTop.x * 255).toInt(),
//                                            (block.color.cTop.y * 255).toInt(),
//                                            (block.color.cTop.z * 255).toInt()
//                                        )
//
//                                        image.setRGB(globalPos.x, globalPos.y, color.rgb)
//                                    }
//                        }
//
//                        val path =
//                            GamePaths.maps.resolve("${SimpleDateFormat("yyyy-MM-dd--HH-mm-ss-SSS").format(Date())}.png")
//                        ImageIO.write(image, "png", path.toFile())
//
//                        log.info { "Map saved at '$path'" }
//                    }
                }
            }


            if (ImGui.collapsingHeader("Time")) {
                if (ImGui.sliderFloat("Scale", timeScale, 0f, 5f))
                    time.scale = timeScale[0]
            }

            if (ImGui.collapsingHeader("OpenGL"))
                if (ImGui.combo("glPolygonMode", comboPolyMode, arrayOf("GL_FILL", "GL_LINE", "GL_POINT")))
                    polygonMode = when (comboPolyMode.get()) {
                        0 -> GL_FILL
                        1 -> GL_LINE
                        2 -> GL_POINT
                        else -> 0
                    }

            if (ImGui.checkbox("Fullscreen", fullscreen)) {
                if (fullscreen.get())
                    glfwSetWindowMonitor(Window.pointer, glfwGetPrimaryMonitor(), 0, 0, 3840, 2160, 120)
                else
                    glfwSetWindowMonitor(Window.pointer, 0, 100, 100, 1920, 1080, 120)
            }

            if (ImGui.radioButton("F3 Overlay", f3))
                f3 = !f3

            if (ImGui.button("Main Menu"))
                SceneManager.selected = MainMenuScene()

            ImGui.end()
        }
    }

}