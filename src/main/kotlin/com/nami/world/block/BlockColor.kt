package com.nami.world.block

import org.joml.Vector4f
import org.lwjgl.opengl.GL14.*
import org.lwjgl.system.MemoryStack
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.roundToInt

class BlockColor(color: Vector4f, mutation: ClosedFloatingPointRange<Float>) {

    companion object {
        @JvmStatic
        val list = mutableListOf<Vector4f>()

        @JvmStatic
        val texture = glGenTextures()

        @JvmStatic
        fun register(color: Vector4f): Int {
            list.add(color)

            return list.size - 1
        }

        val INVALID = BlockColor(Vector4f(1f, 0f, 1f, 1f), 0.5f..1.0f)
        val BEDROCK = BlockColor(Vector4f(0.25f, 0.25f, 0.25f, 1f), 0.5f..0.75f)
        val WATER = BlockColor(Vector4f(33f / 255f, 55f / 255f, 122f / 255f, 0.75f), 0.5f..0.75f)
        val SAND = BlockColor(Vector4f(0.5f, 0.5f, 0f, 1f), 0.5f..1.0f)
        val STONE = BlockColor(Vector4f(0.375f, 0.375f, 0.375f, 1f), 0.5f..1.0f)
        val DIRT = BlockColor(Vector4f(98f / 255f, 82f / 255f, 66f / 255f, 1f), 0.5f..1.0f)
        val GRASS = BlockColor(Vector4f(0f, 0.3f, 0f, 1f), 0.5f..1.0f)

        val MUSHROOM_STEM = BlockColor(Vector4f(200f / 255f, 173f / 255f, 127f / 255f, 1f), 0.75f..1.0f)
        val MUSHROOM_BLOCK_RED = BlockColor(Vector4f(1f, 0f, 0f, 1f), 0.75f..1.0f)
        val MYCELIUM = BlockColor(Vector4f(0.5f, 0.5f, 0.5f, 1f), 0.5f..1.0f)

        val SNOW = BlockColor(Vector4f(0.85f, 0.85f, 0.85f, 1f), 0.8f..1.0f)

        val LOG = BlockColor(Vector4f(71 / 255f, 54 / 255f, 21 / 255f, 1f), 0.6f..1.0f)
        val LEAVES = BlockColor(Vector4f(0f, 0.5f, 0f, 0.95f), 0.6f..1.0f)
        val LEAVES_SNOW = BlockColor(Vector4f(0.85f, 0.85f, 0.85f, 0.95f), 0.8f..1.0f)

        val CACTUS = BlockColor(Vector4f(0f, 0.25f, 0f, 1f), 0.6f..1.0f)

        @JvmStatic
        fun generateTexture(): Int {
            glBindTexture(GL_TEXTURE_1D, texture)

            glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

            MemoryStack.stackPush().use { stack ->
                val buffer = stack.callocFloat(list.size * 4)
                list.forEach { color -> buffer.put(color.x); buffer.put(color.y); buffer.put(color.z); buffer.put(color.w) }
                buffer.flip()

                glTexImage1D(GL_TEXTURE_1D, 0, GL_RGBA, list.size, 0, GL_RGBA, GL_FLOAT, buffer)
            }

            glBindTexture(GL_TEXTURE_1D, 0)

            return texture
        }
    }

    val id: Int = register(color)

}