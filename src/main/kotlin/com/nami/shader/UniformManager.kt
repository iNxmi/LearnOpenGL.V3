package com.nami.shader

import mu.KotlinLogging
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL33.*

class UniformManager(private val shader: ShaderProgram) {

    private val log = KotlinLogging.logger { }
    private val uniforms = mutableMapOf<String, Int>()

    private fun location(name: String): Int {
        if (!uniforms.containsKey(name)) {
            val location = glGetUniformLocation(shader.pointer, name)
            if (location == -1)
                log.warn { "Uniform '$name' in shader '${shader.name}' could not be found" }

            uniforms[name] = location
        }

        return uniforms[name]!!
    }

    fun set(name: String, value: Int): UniformManager {
        glUniform1i(location(name), value)
        return this
    }

    fun set(name: String, value: Float): UniformManager {
        glUniform1f(location(name), value)
        return this
    }

    fun set(name: String, value: Vector2f): UniformManager {
        glUniform2f(location(name), value.x, value.y)
        return this
    }

    fun set(name: String, value: Vector3f): UniformManager {
        glUniform3f(location(name), value.x, value.y, value.z)
        return this
    }

    fun set(name: String, value: Boolean): UniformManager {
        glUniform1i(location(name), if (value) 1 else 0)
        return this
    }

    fun set(name: String, value: Matrix4f): UniformManager {
        val buffer = BufferUtils.createFloatBuffer(16)
        value.get(buffer)

        glUniformMatrix4fv(location(name), false, buffer)
        return this
    }

    fun set(name: String, value: Matrix3f): UniformManager {
        val buffer = BufferUtils.createFloatBuffer(9)
        value.get(buffer)

        glUniformMatrix3fv(location(name), false, buffer)
        return this
    }

}