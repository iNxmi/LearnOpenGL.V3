package com.nami.client.resources.shader

import mu.KotlinLogging
import org.joml.*
import org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryStack

class Uniform(private val shader: com.nami.client.resources.shader.Shader) {

    private val log = KotlinLogging.logger { }
    private val uniforms = mutableMapOf<String, Int>()

    private fun location(name: String): Int {
        if (uniforms.containsKey(name))
            return uniforms[name]!!

        val location = glGetUniformLocation(shader.pointer, name)
        if (location == -1)
            log.warn { "Uniform '$name' in shader '${shader.path}' could not be found" }

        uniforms[name] = location
        return location
    }

    fun set(name: String, value: Int): com.nami.client.resources.shader.Uniform {
        glUniform1i(location(name), value)
        return this
    }

    fun set(name: String, value: Float): com.nami.client.resources.shader.Uniform {
        glUniform1f(location(name), value)
        return this
    }

    fun set(name: String, value: Vector2f): com.nami.client.resources.shader.Uniform {
        glUniform2f(location(name), value.x, value.y)
        return this
    }

    fun set(name: String, value: Vector3f): com.nami.client.resources.shader.Uniform {
        glUniform3f(location(name), value.x, value.y, value.z)
        return this
    }

    fun set(name: String, value: Vector4f): com.nami.client.resources.shader.Uniform {
        glUniform4f(location(name), value.x, value.y, value.z, value.w)
        return this
    }

    fun set(name: String, value: Boolean): com.nami.client.resources.shader.Uniform {
        glUniform1i(location(name), if (value) 1 else 0)
        return this
    }

    fun set(name: String, value: Matrix4f): com.nami.client.resources.shader.Uniform {
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.callocFloat(16)
            value.get(buffer)
            glUniformMatrix4fv(location(name), false, buffer)
        }

        return this
    }

    fun set(name: String, value: Matrix3f): com.nami.client.resources.shader.Uniform {
        MemoryStack.stackPush().use { stack ->
            val buffer = stack.callocFloat(9)
            value.get(buffer)
            glUniformMatrix3fv(location(name), false, buffer)
        }

        return this
    }

}