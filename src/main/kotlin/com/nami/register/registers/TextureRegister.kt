package com.nami.register.registers

import com.nami.constants.GamePaths
import com.nami.register.Register
import com.nami.texture.Texture
import mu.KotlinLogging
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33.*
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.isRegularFile
import kotlin.io.path.name

class TextureRegister : Register<Pair<String, Texture.Type>, Texture>() {

    private val log = KotlinLogging.logger { }

    override fun load(key: Pair<String, Texture.Type>): Texture {
        val path = getTexturePath(key.first, key.second) ?: return get(Pair("_fallback", key.second))
        log.info { "Loading Texture '$path'" }

        val pointer = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, pointer)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        val image = ImageIO.read(path!!.toFile())
        val buffer = bufferedImageToByteBuffer(image)
        glTexImage2D(
            GL_TEXTURE_2D, 0,
            GL_RGBA, image.width, image.height, 0,
            GL_RGBA,
            GL_UNSIGNED_BYTE, buffer
        )
        glGenerateMipmap(GL_TEXTURE_2D)

        glBindTexture(GL_TEXTURE_2D, 0)

        return Texture(pointer)
    }

    private fun getTexturePath(name: String, type: Texture.Type): Path? {
        val directory = GamePaths.textures.resolve(name)

        val path =
            Files.list(directory).filter { path -> path.isRegularFile() && (path.name.startsWith(type.id)) }
                .findAny().orElse(null)

        return path
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

    fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

}