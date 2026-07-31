package com.nami.game.client.scene

import com.nami.engine.graphics.CullingMode
import com.nami.engine.graphics.Graphics
import com.nami.engine.graphics.PolygonMode
import com.nami.engine.platform.input.CursorMode
import com.nami.engine.platform.input.Key
import com.nami.engine.platform.window.Window
import com.nami.legacy.Input
import com.nami.resources.GamePath
import com.nami.scene.Scene
import com.nami.world.World
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.lwjgl.opengl.GL11.GL_RGB
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL11.glReadPixels
import org.lwjgl.system.MemoryStack.stackPush
import java.awt.image.BufferedImage
import java.text.SimpleDateFormat
import java.util.Date
import javax.imageio.ImageIO

class PlayScene(
    val window: Window,
    val world: World
) : Scene {

    private val logger = KotlinLogging.logger { }

    override fun initialize() {
        window.cursorMode = CursorMode.DISABLED

        if(window.isRawMouseMotionSupported)
            window.isRawMouseMotionEnabled = true
    }

    fun screenshot() {
        if (!Input.isKeyPressed(Key.KEY_F2))
            return

        val width = window.size.width
        val height = window.size.height

        GlobalScope.launch {
            stackPush().use { stack ->
                val buffer = stack.malloc(width * height * 3)
                glReadPixels(0, 0, width, height, GL_RGB, GL_UNSIGNED_BYTE, buffer)

                val pixels = IntArray(width * height)
                for (i in pixels.indices) {
                    val red = buffer.get().toInt() and 0xFF shl 16
                    val green = buffer.get().toInt() and 0xFF shl 8
                    val blue = buffer.get().toInt() and 0xFF

                    pixels[i] = red or green or blue
                }

                val flipped = IntArray(width * height)
                for (y in 0 until height) {
                    if (width < 0)
                        continue

                    System.arraycopy(pixels, ((height - 1) - y) * width, flipped, y * width, width)
                }

                val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
                image.setRGB(0, 0, width, height, flipped, 0, width)

                val name = "${SimpleDateFormat("yyyy_MM_dd__HH_mm_ss_SSS").format(Date())}.png"
                val path = GamePath.screenshots.resolve(name)
                ImageIO.write(image, "png", path.toFile())

                logger.info { "Screenshot saved at '$path'" }
            }
        }
    }

    fun input() {
        screenshot()
    }

    override fun render(graphics: Graphics) {
        input()

        graphics.polygonMode = PolygonMode.Fill()
        graphics.cullingMode = CullingMode.BACK

        world.render(graphics)
    }

}