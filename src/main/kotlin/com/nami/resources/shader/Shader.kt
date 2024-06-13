package com.nami.resources.shader

import org.lwjgl.opengl.GL33.*
import java.nio.file.Path

data class Shader(val pointer: Int, val path: Path) {

    val uniform: Uniform = Uniform(this)

    fun bind(): Shader {
        glUseProgram(pointer)
        return this
    }

    enum class Type(val id: Int, val regex: String) {
        VERTEX_SHADER(GL_VERTEX_SHADER, "//BEGIN_VS(.*)//END_VS"),
        FRAGMENT_SHADER(GL_FRAGMENT_SHADER, "//BEGIN_FS(.*)//END_FS")
    }

}