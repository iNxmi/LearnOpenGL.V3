package com.nami.resources.shader

import com.nami.resources.GamePath
import com.nami.resources.Resources
import org.lwjgl.opengl.GL33.*
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderShader : Resources<Shader>(GamePath.shader, "shader", arrayOf("glsl")) {

    override fun onLoad(id: String, path: Path): Shader {
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

        return Shader(id, pointer, path)
    }

    override fun onLoadCompleted() {

    }

    private fun loadSubShader(path: Path, source: String, type: Shader.Type): Int {
        val source: MatchResult = type.regex.find(source)
            ?: throw RuntimeException("Could not identify $type in '$path'")

        val pointer = glCreateShader(type.id)
        glShaderSource(pointer, source.value.trim())
        glCompileShader(pointer)

        if (glGetShaderi(pointer, GL_COMPILE_STATUS) == 0)
            throw RuntimeException("Compilation of shader \"$path\" failed. ${glGetShaderInfoLog(pointer)}")

        return pointer
    }

    fun unbind() {
        glUseProgram(0)
    }

}