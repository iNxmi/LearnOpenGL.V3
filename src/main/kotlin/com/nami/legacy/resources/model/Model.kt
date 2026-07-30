package com.nami.resources.model

import org.lwjgl.opengl.GL33.*
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Model(
    id: String,
    positions: FloatBuffer,
    uvs: FloatBuffer,
    normals: FloatBuffer,
    indices: IntBuffer,
    val numberOfIndices: Int = indices.limit()
) : ResourceModel(id) {

    val vao = glGenVertexArrays()

    val vboPositions = glGenBuffers()
    val vboUVs = glGenBuffers()
    val vboNormals = glGenBuffers()

    val ebo = glGenBuffers()

    init {
        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, vboPositions)
        glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ARRAY_BUFFER, vboUVs)
        glBufferData(GL_ARRAY_BUFFER, uvs, GL_STATIC_DRAW)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ARRAY_BUFFER, vboNormals)
        glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW)
        glEnableVertexAttribArray(2)
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

        glBindVertexArray(0)
    }

}

