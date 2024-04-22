package com.nami.shader

import mu.KotlinLogging
import org.joml.*
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33.*
import org.lwjgl.opengl.GL40.glUniformMatrix4dv

class Uniform(private val shader: Shader) {

    private val log = KotlinLogging.logger { }
    private val uniforms = mutableMapOf<String, Int>()

    private fun location(name: String): Int {
        if (!uniforms.containsKey(name)) {
            val location = glGetUniformLocation(shader.pointer, name)
            if (location == -1)
                log.warn { "Uniform '$name' in shader '${shader.path}' could not be found" }

            uniforms[name] = location
        }

        return uniforms[name]!!
    }

    fun set(name: String, value: Int): Uniform {
        glUniform1i(location(name), value)
        return this
    }

    fun set(name: String, value: Float): Uniform {
        glUniform1f(location(name), value)
        return this
    }

    fun set(name: String, value: Vector2f): Uniform {
        glUniform2f(location(name), value.x, value.y)
        return this
    }

    fun set(name: String, value: Vector3f): Uniform {
        glUniform3f(location(name), value.x, value.y, value.z)
        return this
    }

    fun set(name: String, value: Boolean): Uniform {
        glUniform1i(location(name), if (value) 1 else 0)
        return this
    }

    fun set(name: String, value: Matrix4f): Uniform {
        val buffer = BufferUtils.createFloatBuffer(16)
        value.get(buffer)

        glUniformMatrix4fv(location(name), false, buffer)
        return this
    }

    fun set(name: String, value: Matrix3f): Uniform {
        val buffer = BufferUtils.createFloatBuffer(9)
        value.get(buffer)

        glUniformMatrix3fv(location(name), false, buffer)
        return this
    }

}