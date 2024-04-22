package com.nami.resource.resoures

import com.nami.constants.GamePaths
import com.nami.resource.Resource
import com.nami.shader.Shader
import org.lwjgl.opengl.GL33.*
import java.nio.file.Files
import java.nio.file.Path

class ShaderResource : Resource<Shader>(GamePaths.shaders, "glsl") {

    override fun onLoad(path: Path): Shader {
        val source = Files.readString(path)

        val vert = loadSubShader(path, source, Shader.Type.VERTEX_SHADER)
        val frag = loadSubShader(path, source, Shader.Type.FRAGMENT_SHADER)

        val pointer = glCreateProgram()
        glAttachShader(pointer, vert)
        glAttachShader(pointer, frag)
        glLinkProgram(pointer)

        glDeleteShader(vert)
        glDeleteShader(frag)

        if (glGetProgrami(pointer, GL_LINK_STATUS) == 0)
            throw RuntimeException("Linkage of shader '$path' failed. ${glGetProgramInfoLog(pointer)}")

        return Shader(pointer, path)
    }

    private fun loadSubShader(path: Path, source: String, type: Shader.Type): Int {
        val regex = type.regex.toRegex(setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.MULTILINE))

        val source: MatchResult? = regex.find(source)
            ?: throw RuntimeException("Could not identify $type in '$path'")

        val pointer = glCreateShader(type.id)
        glShaderSource(pointer, source!!.value.trim())
        glCompileShader(pointer)

        if (glGetShaderi(pointer, GL_COMPILE_STATUS) == 0)
            throw RuntimeException("Compilation of shader \"$path\" failed. ${glGetShaderInfoLog(pointer)}")

        return pointer
    }

    fun unbind() {
        glUseProgram(0)
    }

}