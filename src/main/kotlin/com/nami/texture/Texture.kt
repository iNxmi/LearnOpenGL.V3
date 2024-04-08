package com.nami.texture

import org.lwjgl.opengl.GL33

data class Texture(val pointer: Int, val type: TextureType) {

    fun bind(slot: Int) {
        GL33.glActiveTexture(GL33.GL_TEXTURE0 + slot)
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, pointer)
    }

}

enum class TextureType(val id: String) {
    DIFFUSE("diffuse"),
    SPECULAR("specular"),
    NORMAL("normal"),
    ROUGHNESS("roughness"),
    AMBIENT("ambient");

    companion object {
        private val map = TextureType.values().associateBy { it.id }
        infix fun from(id: String) = map[id]!!
    }
}