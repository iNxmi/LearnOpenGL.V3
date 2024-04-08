package com.nami.shader

import org.lwjgl.opengl.GL33.*

class ShaderManager {

    companion object {

        private val map = mutableMapOf<String, ShaderProgram>()

        @JvmStatic
        fun get(name: String): ShaderProgram {
            if(!map.containsKey(name))
                map[name] = ShaderProgram(name)

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