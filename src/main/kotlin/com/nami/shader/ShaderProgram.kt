package com.nami.shader

import com.nami.constants.GamePaths
import mu.KotlinLogging
import org.lwjgl.opengl.GL33
import java.nio.file.Files

class ShaderProgram(val name: String) {
    private val log = KotlinLogging.logger { }

    val pointer: Int
    val uniform: UniformManager = UniformManager(this)

    init {
        log.info { "Loading Shader '${GamePaths.shaders.resolve(name)}'" }

        val vert = loadShader(name, ShaderType.VERTEX_SHADER)
        val frag = loadShader(name, ShaderType.FRAGMENT_SHADER)

        pointer = GL33.glCreateProgram()
        GL33.glAttachShader(pointer, vert)
        GL33.glAttachShader(pointer, frag)
        GL33.glLinkProgram(pointer)

        GL33.glDeleteShader(vert)
        GL33.glDeleteShader(frag)

        if (GL33.glGetProgrami(pointer, GL33.GL_LINK_STATUS) == 0)
            throw RuntimeException("Linkage of shader \"$name\" failed. ${GL33.glGetProgramInfoLog(pointer)}")
    }


    private fun loadShader(name: String, type: ShaderType): Int {
        val path = GamePaths.shaders.resolve("$name.${type.fileNameExtension}")

        val pointer = GL33.glCreateShader(type.id)
        GL33.glShaderSource(pointer, Files.readString(path))
        GL33.glCompileShader(pointer)

        if (GL33.glGetShaderi(pointer, GL33.GL_COMPILE_STATUS) == 0)
            throw RuntimeException("Compilation of shader \"$path\" failed. ${GL33.glGetShaderInfoLog(pointer)}")

        return pointer
    }

    fun bind() {
        GL33.glUseProgram(pointer)
    }

    private enum class ShaderType(val id: Int, val fileNameExtension: String) {
        VERTEX_SHADER(GL33.GL_VERTEX_SHADER, "vert"),
        FRAGMENT_SHADER(GL33.GL_FRAGMENT_SHADER, "frag")
    }

}