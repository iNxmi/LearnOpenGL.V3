package com.nami.register.registers

import com.nami.constants.GamePaths
import com.nami.register.Register
import com.nami.shader.Shader
import mu.KotlinLogging
import org.lwjgl.opengl.GL33.*
import java.nio.file.Files

class ShaderRegister : Register<String, Shader>() {

    private val log = KotlinLogging.logger { }
    override fun load(key: String): Shader {
        log.info { "Loading Shader '${GamePaths.shaders.resolve(key)}'" }

        val vert = loadSubShader(key, Shader.Type.VERTEX_SHADER)
        val frag = loadSubShader(key, Shader.Type.FRAGMENT_SHADER)

        val pointer = glCreateProgram()
        glAttachShader(pointer, vert)
        glAttachShader(pointer, frag)
        glLinkProgram(pointer)

        glDeleteShader(vert)
        glDeleteShader(frag)

        if (glGetProgrami(pointer, GL_LINK_STATUS) == 0) {
            val path = GamePaths.shaders.resolve(key)
            throw RuntimeException("Linkage of shader '$path' failed. ${glGetProgramInfoLog(pointer)}")
        }

        return Shader(key, pointer)
    }

    private fun loadSubShader(key: String, type: Shader.Type): Int {
        val path = GamePaths.shaders.resolve("$key.${type.fileNameExtension}")

        val pointer = glCreateShader(type.id)
        glShaderSource(pointer, Files.readString(path))
        glCompileShader(pointer)

        if (glGetShaderi(pointer, GL_COMPILE_STATUS) == 0)
            throw RuntimeException("Compilation of shader \"$path\" failed. ${glGetShaderInfoLog(pointer)}")

        return pointer
    }

    fun unbind() {
        glUseProgram(0)
    }

}