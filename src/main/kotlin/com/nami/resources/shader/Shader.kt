package com.nami.resources.shader

import org.lwjgl.opengl.GL33.*
import java.nio.file.Path

class Shader(
    id: String,
    val pointer: Int,
    val path: Path
) : ResourceShader(id) {

    val uniform: Uniform = Uniform(this)

    fun bind(): Shader {
        glUseProgram(pointer)
        return this
    }

    enum class Type(val id: Int, val regex: Regex) {
        VERTEX_SHADER(GL_VERTEX_SHADER, Regex("\\/\\/BEGIN_VS\\s([\\s\\S]*?)\\s\\/\\/END_VS")),
        FRAGMENT_SHADER(GL_FRAGMENT_SHADER, Regex("\\/\\/BEGIN_FS\\s([\\s\\S]*?)\\s\\/\\/END_FS"))
    }

}