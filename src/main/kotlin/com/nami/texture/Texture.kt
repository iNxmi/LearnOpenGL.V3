package com.nami.texture

import org.lwjgl.opengl.GL33.*

class Texture(val pointer: Int) {

    fun bind(slot: Int): Texture {
        glActiveTexture(GL_TEXTURE0 + slot)
        glBindTexture(GL_TEXTURE_2D, pointer)

        return this
    }

    enum class Type(val id: String) {
        DIFFUSE("diffuse"),
        SPECULAR("specular"),
        NORMAL("normal"),
        ROUGHNESS("roughness"),
        AMBIENT("ambient"),
        FRAME_BUFFER("");

        companion object {
            private val map = Type.values().associateBy { it.id }
            infix fun from(id: String) = map[id]!!
        }
    }

}