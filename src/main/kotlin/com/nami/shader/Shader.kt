package com.nami.shader

import org.lwjgl.opengl.GL33.*

data class Shader(val key: String, val pointer: Int) {

    val uniform: Uniform = Uniform(this)

    fun bind(): Shader {
        glUseProgram(pointer)

        return this
    }

    enum class Type(val id: Int, val fileNameExtension: String) {
        VERTEX_SHADER(GL_VERTEX_SHADER, "vert"),
        FRAGMENT_SHADER(GL_FRAGMENT_SHADER, "frag")
    }

}