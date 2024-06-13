package com.nami.resources.texture

import com.nami.resources.Resource
import org.joml.Vector2f
import org.joml.Vector2i
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.max

class TextureAtlas {

    companion object {
        @JvmStatic
        var SIZE_IN_PIXELS: Vector2i? = null
            private set

        @JvmStatic
        var texture: Texture? = null
            private set

        @JvmStatic
        val uvNDC = mutableMapOf<String, UV>()

        @JvmStatic
        fun generate() {
            var width = 0
            var height = 0
            Resource.TEXTURE.map.forEach { (k, v) ->
                val img = v.image
                width += img.width
                height = max(height, img.height)
            }
            SIZE_IN_PIXELS = Vector2i(width, height)

            val image = BufferedImage(SIZE_IN_PIXELS!!.x, SIZE_IN_PIXELS!!.y, BufferedImage.TYPE_INT_ARGB)
            val graphics = image.createGraphics()

            var cursor = 0
            Resource.TEXTURE.map.forEach { (k, v) ->
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

            texture = Texture(image)
        }

        @JvmStatic
        fun getUV(key: String): UV {
            return uvNDC[key]!!
        }


    }

}