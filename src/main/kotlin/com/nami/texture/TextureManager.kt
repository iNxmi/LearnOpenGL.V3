package com.nami.texture

import com.nami.constants.GamePaths
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

class TextureManager {

    companion object {

        private val log = KotlinLogging.logger { }

        @JvmStatic
        val textures: MutableMap<Pair<String, TextureType>, Texture> = mutableMapOf()

        init {
            load("_fallback", TextureType.DIFFUSE, ScalingFunction.NEAREST)
            load("_fallback", TextureType.SPECULAR, ScalingFunction.NEAREST)
            load("_fallback", TextureType.ROUGHNESS, ScalingFunction.NEAREST)
            load("_fallback", TextureType.AMBIENT, ScalingFunction.NEAREST)
            load("_fallback", TextureType.NORMAL, ScalingFunction.NEAREST)
        }

        @JvmStatic
        fun load(name: String, type: TextureType, scalingFunction: ScalingFunction): Texture {
            val key = Pair(name, type)
            if (!textures.containsKey(key))
                textures[key] = loadTexture(name, type, scalingFunction)

            return textures[key]!!
        }

        @JvmStatic
        fun get(name: String, type: TextureType): Texture {
            val key = Pair(name, type)
            if (!textures.containsKey(key))
                throw RuntimeException("Texture '$name : $type' has not been loaded yet")

            return textures[key]!!
        }

        private fun loadTexture(name: String, type: TextureType, scalingFunction: ScalingFunction): Texture {
            val path = getTexturePath(name, type) ?: return get("_fallback", type)
            log.info { "Loading Texture '$path'" }

            val pointer = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, pointer)

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, scalingFunction.value)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, scalingFunction.value)

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

            return Texture(pointer, type)
        }

        private fun getTexturePath(name: String, type: TextureType): Path? {
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

        @JvmStatic
        fun bind(name: String, type: TextureType, slot: Int): Texture {
            val texture = get(name, type)
            bind(texture, slot)

            return texture
        }

        @JvmStatic
        fun bind(texture: Texture, slot: Int) {
            texture.bind(slot)
        }

        @JvmStatic
        fun unbind() {
            glBindTexture(GL_TEXTURE_2D, 0)
        }

    }

    enum class ScalingFunction(val id: String, val value: Int) {
        NEAREST("nearest", GL_NEAREST),
        LINEAR("linear", GL_LINEAR);

        companion object {
            private val map = ScalingFunction.values().associateBy { it.id }
            infix fun from(id: String) = map[id]!!
        }
    }

}