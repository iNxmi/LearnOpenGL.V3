package com.nami.texture

import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL11.glGenTextures
import org.lwjgl.opengl.GL33
import org.lwjgl.opengl.GL33.*
import java.awt.image.BufferedImage

abstract class Texture<T>(val pointer: Int = glGenTextures(), val target: Target) {

    fun parameter(parameter: Parameter, value: Int): T {
        bind()

        glTexParameteri(target.id, parameter.id, value)

        unbind()

        return this as T
    }

    abstract fun image(image: BufferedImage, format: Format): T

    fun generateMipmap(): T {
        glGenerateMipmap(target.id)
        return this as T
    }

    fun bind(): T {
        glBindTexture(target.id, pointer)
        return this as T
    }

    fun unbind(): T {
        glBindTexture(target.id, 0)
        return this as T
    }

    fun delete() {
        glDeleteTextures(pointer)
    }

    enum class Parameter(val id: Int) {
        TEXTURE_BASE_LEVEL(GL_TEXTURE_BASE_LEVEL),
        TEXTURE_COMPARE_FUNC(GL_TEXTURE_COMPARE_FUNC),
        TEXTURE_COMPARE_MODE(GL_TEXTURE_COMPARE_MODE),
        TEXTURE_LOD_BIAS(GL_TEXTURE_LOD_BIAS),
        TEXTURE_MIN_FILTER(GL_TEXTURE_MIN_FILTER),
        TEXTURE_MAG_FILTER(GL_TEXTURE_MAG_FILTER),
        TEXTURE_MIN_LOD(GL_TEXTURE_MIN_LOD),
        TEXTURE_MAX_LOD(GL_TEXTURE_MAX_LOD),
        TEXTURE_MAX_LEVEL(GL_TEXTURE_MAX_LEVEL),
        TEXTURE_SWIZZLE_R(GL_TEXTURE_SWIZZLE_R),
        TEXTURE_SWIZZLE_G(GL_TEXTURE_SWIZZLE_G),
        TEXTURE_SWIZZLE_B(GL_TEXTURE_SWIZZLE_B),
        TEXTURE_SWIZZLE_A(GL_TEXTURE_SWIZZLE_A),
        TEXTURE_WRAP_S(GL_TEXTURE_WRAP_S),
        TEXTURE_WRAP_T(GL_TEXTURE_WRAP_T),
        TEXTURE_WRAP_R(GL_TEXTURE_WRAP_R)
    }

    enum class Format(val id: Int) {
        RGB(GL33.GL_RGB),
        RGBA(GL33.GL_RGBA)
    }

    enum class Target(val id: Int) {
        TEXTURE_1D(GL33.GL_TEXTURE_1D),
        TEXTURE_2D(GL33.GL_TEXTURE_2D),
        TEXTURE_3D(GL33.GL_TEXTURE_3D),
        TEXTURE_CUBE_MAP(GL33.GL_TEXTURE_CUBE_MAP)
    }

    companion object {
        @JvmStatic
        fun activate(slot: Int) {
            glActiveTexture(GL_TEXTURE0 + slot)
        }

        @JvmStatic
        fun unbind(target: Target) {
            glBindTexture(target.id, 0)
        }
    }

}