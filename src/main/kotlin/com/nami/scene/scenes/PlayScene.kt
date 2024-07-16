package com.nami.scene.scenes

import com.nami.Game
import com.nami.Window
import com.nami.input.Input
import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.scene.Scene
import com.nami.scene.SceneManager
import com.nami.world.World
import com.nami.world.block.Block
import com.nami.world.inventory.item.Item
import com.nami.world.recipe.RecipeVariant
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import imgui.type.ImInt
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.joml.Vector3d
import org.joml.Vector3i
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL33.*
import java.awt.image.BufferedImage
import java.text.SimpleDateFormat
import java.util.*
import javax.imageio.ImageIO


class PlayScene(seed: Long) : Scene() {

    private val log = KotlinLogging.logger { }

    private val world = World(seed, seed.toString(), time, Vector3i(512, 512, 512), 64)

    private var polygonMode = GL_FILL

    private var menu: String? = null

    private val menus = mapOf(
        Pair("settings", Runnable {
            ImGui.setNextWindowPos(0f, 0f)
            ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())
            ImGui.setNextWindowBgAlpha(0.4f)

            ImGui.getFont().scale = 2f
            ImGui.begin("settings", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

            if (ImGui.collapsingHeader("World")) {
                if (ImGui.button("Save")){

                }
//                    world.save()
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

            if (ImGui.button("Reload all chunks")) {
                world.chunkManager.chunks.forEach { (_, chunk) -> world.chunkManager.meshGenerator.addToQueue(chunk.position) }
            }

            if (ImGui.button("Main Menu"))
                SceneManager.selected = MainMenuScene()

            ImGui.end()
        }),
        Pair("inventory", Runnable {
            val range = 4

            val workstations = mutableSetOf<Block>()
            for (z in -range..range)
                for (y in -range..range)
                    for (x in -range..range)
                        if (x * x + y * y + z * z <= range * range * range) {
                            val position = Vector3i(
                                world.player.transform.position.x.toInt(),
                                world.player.transform.position.y.toInt(),
                                world.player.transform.position.z.toInt(),
                            ).add(x, y, z)

                            val block = world.blockManager.getBlock(position) ?: continue

                            val tags = block.template.tags ?: continue
                            if (!tags.contains("workstation")) continue

                            workstations.add(block.template)
                        }

            ImGui.setNextWindowPos(0f, 0f)
            ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())
            ImGui.setNextWindowBgAlpha(0.4f)

            ImGui.getFont().scale = 2f
            ImGui.begin("inventory", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

            ImGui.text("Inventory")
            ImGui.text("Weight: ${world.player.inventory.weight}kg")
            world.player.inventory.map.forEach { (item, amount) ->
                if (ImGui.button("${item.template.language("name")}: $amount * ${item.template.weight} = ${amount * item.template.weight}kg")) {
                    world.player.selectedItem = item
                }
            }

            ImGui.text("Crafting")
            ImGui.text("Workstations in range: $workstations")
            val recipes = mutableMapOf<Item, List<RecipeVariant>>()
            Resources.RECIPE.map.values.forEach { recipe -> recipes[recipe.item] = recipe.variants }

            recipes.forEach { (item, variants) ->
                if (!ImGui.collapsingHeader(item.language("name"))) return@forEach

                variants.forEach { variant ->
                    if (variant.workstations != null)
                        if (!variant.workstations.any { it in workstations }) return@forEach

                    val inventory = world.player.inventory
//                    val success = variant.ingredients.all { (item, amount) -> inventory.get(item) >= amount }
                    val success = false

                    val buttonName = variant.ingredients.map { (item, amount) -> "${item.language("name")}($amount) " }
                        .joinToString { it } + "= ${item.language("name")}(${variant.amount}) Workstations(${variant.workstations}) Success($success)"
                    if (!ImGui.button(buttonName)) return@forEach

                    if (!success) return@forEach

//                    variant.ingredients.forEach { (item, amount) -> inventory.remove(item, amount) }
//                    inventory.add(item, variant.amount)
                }

            }

            ImGui.end()
        }),
        Pair("info", Runnable {
            ImGui.setNextWindowPos(0f, 0f)
            ImGui.setNextWindowSize(Window.width.toFloat(), Window.height.toFloat())

            ImGui.getFont().scale = 2.5f
            ImGui.begin("info", ImBoolean(), ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

            ImGui.text("FPS=${1f / Game.DELTA_TIME}")
            ImGui.text("seed=${world.seed}")
            val biome = world.biomeManager.getBiome(
                Vector3i(
                    world.player.transform.position.x.toInt(),
                    world.player.transform.position.y.toInt(),
                    world.player.transform.position.z.toInt()
                )
            )
            ImGui.text("biome=${biome?.template?.id} (${biome?.template?.language("name")}) factors=${biome?.factors}")
            ImGui.text("position=${world.player.transform.position}")
            ImGui.text("block_position=${Vector3i().set(Vector3d(world.player.transform.position))}")

            ImGui.end()
        })
    )

    override fun onInit() {
        glfwSetInputMode(Window.pointer, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
//
//        if (glfwRawMouseMotionSupported())
//            glfwSetInputMode(Window.pointer, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE)
    }

    override fun onUpdate() {
        if (Input.keyStates[GLFW_KEY_ESCAPE] == Input.State.DOWN)
            menu = if (menu == null) "settings" else null

        if (menu == null) {
            if (Input.keyStates[GLFW_KEY_E] == Input.State.DOWN)
                menu = "inventory"

            if (Input.keyStates[GLFW_KEY_F3] == Input.State.DOWN)
                menu = "info"
        }

        glfwSetInputMode(
            Window.pointer,
            GLFW_CURSOR,
            if (menu != null) GLFW_CURSOR_NORMAL else GLFW_CURSOR_DISABLED
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
                    GamePath.screenshots.resolve("${SimpleDateFormat("yyyy-MM-dd--HH-mm-ss-SSS").format(Date())}.png")
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

        world.render(time)
    }

    private val comboPolyMode = ImInt()
    private val timeScale = floatArrayOf(1.0f)
    private val fullscreen = ImBoolean(false)
    private val selectedItem = ImInt(0)

    override fun onRenderHUD() {
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)

        menus[menu]?.run()
    }

}