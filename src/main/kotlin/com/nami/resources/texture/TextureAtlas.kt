package com.nami.resources.texture

import com.nami.graphics.UV
import com.nami.resources.Resources
import org.joml.Vector2f
import org.joml.Vector2i
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.max

object TextureAtlas {

    var texture: Texture? = null
    val size = Vector2i()

    val uvMap = mutableMapOf<String, UV>()

    fun generate() {
        val images = Resources.TEXTURE.map.mapValues { (_, texture) -> extendBorder(texture.image) }

        size.set(0, 0)
        images.forEach { (_, image) ->
            size.x += image.width
            size.y = max(size.y, image.height)
        }

        val finalImage = BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB)
        val graphics = finalImage.createGraphics()

        val cursor = Vector2i()
        images.forEach { (id, image) ->
            val width = image.width
            val height = image.height

            graphics.drawImage(image, cursor.x, 0, null)

            uvMap[id] = UV(
                Vector2f((cursor.x.toFloat() + 1) / size.x.toFloat(), 1f / size.y.toFloat()),
                Vector2f(
                    (width.toFloat()-2) / size.x.toFloat(),
                    (height.toFloat()-2) / size.y.toFloat()
                )
            )

            cursor.x += image.width
        }

        texture = Texture("atlas", finalImage)
        ImageIO.write(finalImage, "png", File("atlas.png"))
    }

    fun extendBorder(image: BufferedImage): BufferedImage {
        val newSize = Vector2i(image.width + 2, image.height + 2)
        val result = BufferedImage(newSize.x, newSize.y, BufferedImage.TYPE_INT_ARGB)

        for (y in 0 until image.height)
            for (x in 0 until image.width)
                result.setRGB(x + 1, y + 1, image.getRGB(x, y))

        for (x in 0 until image.width) {
            result.setRGB(x + 1, 0, image.getRGB(x, 0))
            result.setRGB(x + 1, newSize.x - 1, image.getRGB(x, image.height - 1))
        }

        for (y in 0 until image.height) {
            result.setRGB(0, y + 1, image.getRGB(0, y))
            result.setRGB(newSize.x - 1, y + 1, image.getRGB(image.width - 1, y))
        }

        result.setRGB(0, 0, image.getRGB(0, 0))
        result.setRGB(newSize.x - 1, 0, image.getRGB(image.width - 1, 0))
        result.setRGB(0, newSize.y - 1, image.getRGB(0, image.height - 1))
        result.setRGB(
            newSize.x - 1,
            newSize.y - 1,
            image.getRGB(image.width - 1, image.height - 1)
        )

        return result
    }

    fun getUVs(key: String) = uvMap[key]!!

}