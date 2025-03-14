package com.nami.client.resources.shader

import org.lwjgl.opengl.GL33.*
import java.nio.file.Path

class Shader(
    id: String,
    val pointer: Int,
    val path: Path
) : ResourceShader(id) {

    val uniform: com.nami.client.resources.shader.Uniform = com.nami.client.resources.shader.Uniform(this)

    fun bind(): Shader {
        glUseProgram(pointer)
        return this
    }

    enum class Type(val id: Int, val regex: String) {
        VERTEX_SHADER(GL_VERTEX_SHADER, "//BEGIN_VS(.*)//END_VS"),
        FRAGMENT_SHADER(GL_FRAGMENT_SHADER, "//BEGIN_FS(.*)//END_FS")
    }

}