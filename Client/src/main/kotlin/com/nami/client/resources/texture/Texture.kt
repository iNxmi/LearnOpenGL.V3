package com.nami.client.resources.texture

import com.nami.client.resources.Resources
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil
import java.awt.image.BufferedImage

class Texture(
    id: String,
    val image: BufferedImage
) : ResourceTexture(id) {

    val pointer = glGenTextures()

    init {
        bind()

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        val buffer = MemoryUtil.memAlloc(image.width * image.height * 4)
        for (y in (image.height - 1) downTo 0)
            for (x in 0 until image.width) {
                val color = image.getRGB(x, y)

                buffer.put(((color shr 16) and 0xff).toByte())
                buffer.put(((color shr 8) and 0xff).toByte())
                buffer.put(((color shr 0) and 0xff).toByte())
                buffer.put(((color shr 24) and 0xff).toByte())
            }
        buffer.flip()

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
        MemoryUtil.memFree(buffer)

        glGenerateMipmap(GL_TEXTURE_2D)

        Resources.TEXTURE.unbind()
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, pointer)
    }

}