package com.nami

import org.lwjgl.opengl.GL33.*

class GLMemoryUtils {

    companion object {
        private val buffers = mutableListOf<Int>()
        private val vertexArrays = mutableListOf<Int>()

        @JvmStatic
        fun genBuffer(): Int {
            val pointer = glGenBuffers()
            buffers.add(pointer)

            return pointer
        }

        @JvmStatic
        fun genVertexArray(): Int {
            val pointer = glGenVertexArrays()
            vertexArrays.add(pointer)

            return pointer
        }

        @JvmStatic
        fun delete() {
            glDeleteBuffers(buffers.toIntArray())
            glDeleteVertexArrays(vertexArrays.toIntArray())
        }
    }

}