package com.nami.mesh

import org.lwjgl.opengl.GL33.*

class Mesh(
    positions: FloatArray,
    normals: FloatArray,
    uvs: FloatArray,
    private val indices: IntArray
) {

    private val vao = glGenVertexArrays()

    init {
        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, glGenBuffers())
        glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L)
        glEnableVertexAttribArray(0)

        glBindBuffer(GL_ARRAY_BUFFER, glGenBuffers())
        glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW)
        glVertexAttribPointer(1, 3, GL_FLOAT, true, 0, 0)
        glEnableVertexAttribArray(1)

        glBindBuffer(GL_ARRAY_BUFFER, glGenBuffers())
        glBufferData(GL_ARRAY_BUFFER, uvs, GL_STATIC_DRAW)
        glVertexAttribPointer(2, 2, GL_FLOAT, true, 0, 0)
        glEnableVertexAttribArray(2)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glGenBuffers())
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

        glBindVertexArray(0)
    }

    fun render() {
        glBindVertexArray(vao)
        glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }

}