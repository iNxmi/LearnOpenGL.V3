package com.nami.engine.graphics

import org.lwjgl.opengl.GL11.GL_BACK
import org.lwjgl.opengl.GL11.GL_FRONT
import org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK

enum class CullingMode(val openglCode: Int) {

    DISABLED(0),
    FRONT(GL_FRONT),
    BACK(GL_BACK),
    FRONT_AND_BACK(GL_FRONT_AND_BACK);

    object Mapper {
        fun getOpenGL(code: Int) = when(code) {
            GL_FRONT -> FRONT
            GL_BACK -> BACK
            GL_FRONT_AND_BACK -> FRONT_AND_BACK
            else -> throw IllegalArgumentException()
        }
    }

}