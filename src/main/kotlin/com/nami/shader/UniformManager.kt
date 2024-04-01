package com.nami.shader

import mu.KotlinLogging
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL33.*

class UniformManager(private val shader: ShaderProgram) {

    private val log = KotlinLogging.logger { }
    private val uniforms = mutableMapOf<String, Int>()

    fun locate(name: String): UniformManager {
        val location = glGetUniformLocation(shader.pointer, name)
        if (location == -1) {
            log.warn { "Uniform '$name' in shader '${shader.name}' could not be found" }
            return this
        }

        uniforms[name] = location
        return this
    }

    private fun location(name: String): Int {
        return uniforms[name]!!
    }

    fun set(name: String, value: Int) {
        glUniform1i(location(name), value)
    }

    fun set(name: String, value: Float) {
        glUniform1f(location(name), value)
    }

    fun set(name: String, value: Vector2f) {
        glUniform2f(location(name), value.x, value.y)
    }

    fun set(name: String, value: Vector3f) {
        glUniform3f(location(name), value.x, value.y, value.z)
    }

    fun set(name: String, value: Matrix4f): UniformManager {
        val buffer = BufferUtils.createFloatBuffer(16)
        value.get(buffer)

        glUniformMatrix4fv(location(name), false, buffer)
        return this
    }

}