package com.nami.resources.texture

import com.nami.graphics.UV
import com.nami.resources.Resources
import org.joml.Vector2f
import org.joml.Vector2i
import java.awt.image.BufferedImage
import kotlin.math.max

object TextureAtlas {

    var SIZE_IN_PIXELS: Vector2i? = null
        private set

    var texture: Texture? = null
        private set

    val uvNDC = mutableMapOf<String, UV>()

    fun generate() {
        var width = 0
        var height = 0
        Resources.TEXTURE.map.forEach { (id, texture) ->
            val img = texture.image
            width += img.width
            height = max(height, img.height)
        }
        SIZE_IN_PIXELS = Vector2i(width, height)

        val image = BufferedImage(SIZE_IN_PIXELS!!.x, SIZE_IN_PIXELS!!.y, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.createGraphics()

        var cursor = 0
        Resources.TEXTURE.map.forEach { (k, v) ->
            val img = v.image
            val width = img.width
            val height = img.height

            graphics.drawImage(v.image, cursor, 0, null)

            uvNDC[k] = UV(
                Vector2f(cursor.toFloat() / SIZE_IN_PIXELS!!.x.toFloat(), 0f),
                Vector2f(
                    width.toFloat() / SIZE_IN_PIXELS!!.x.toFloat(),
                    height.toFloat() / SIZE_IN_PIXELS!!.y.toFloat()
                )
            )

            cursor += v.image.width
        }

        texture = Texture("atlas", image)
    }

    fun getUV(key: String) = uvNDC[key]!!

}