package com.nami.shader

import org.lwjgl.opengl.GL33.*

class ShaderManager {

    companion object {

        private val map = mutableMapOf<String, ShaderProgram>()

        @JvmStatic
        fun load(name: String): ShaderProgram {
            val shader = ShaderProgram(name)
            map[name] = shader

            return shader
        }

        @JvmStatic
        fun get(name: String): ShaderProgram {
            return map[name]!!
        }

        @JvmStatic
        fun bind(name: String): ShaderProgram {
            val shader = get(name)
            shader.bind()

            return shader
        }

        @JvmStatic
        fun unbind() {
            glUseProgram(0)
        }
    }

}