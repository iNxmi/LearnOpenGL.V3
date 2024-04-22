package com.nami.texture

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33.*
import java.awt.image.BufferedImage
import java.nio.ByteBuffer

class Texture2D : Texture<Texture2D>(target = Target.TEXTURE_2D) {

    override fun image(image: BufferedImage, format: Format): Texture2D {
        bind()

        val buffer = bufferedImageToByteBuffer(image)
        glTexImage2D(target.id, 0, format.id, image.width, image.height, 0, format.id, GL_UNSIGNED_BYTE, buffer)

        unbind()

        return this
    }

    private fun bufferedImageToByteBuffer(image: BufferedImage): ByteBuffer {
        val pixels = IntArray(image.width * image.height)
        image.getRGB(0, 0, image.width, image.height, pixels, 0, image.width)

        val buffer = BufferUtils.createByteBuffer(pixels.size * 4)
        for (y in (image.height - 1) downTo 0)
            for (x in 0 until image.width) {
                val pixel = pixels[y * image.width + x]

                val r: Byte = ((pixel shr 16) and 0xFF).toByte()
                buffer.put(r)
                val g: Byte = ((pixel shr 8) and 0xFF).toByte()
                buffer.put(g)
                val b: Byte = (pixel and 0xFF).toByte()
                buffer.put(b)
                val a: Byte = ((pixel shr 24) and 0xFF).toByte()
                buffer.put(a)
            }

        buffer.flip()

        return buffer
    }

}