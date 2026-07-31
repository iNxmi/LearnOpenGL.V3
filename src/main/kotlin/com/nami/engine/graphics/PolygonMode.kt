package com.nami.engine.graphics

import org.lwjgl.opengl.GL11.*

sealed interface PolygonMode {
    val antialiasing: Boolean

    data class Fill(
        override val antialiasing: Boolean = false
    ) : PolygonMode

    data class Line(
        val width: Float = 1.0f,
        override val antialiasing: Boolean = false
    ) : PolygonMode

    data class Point(
        val size: Float = 1.0f,
        override val antialiasing: Boolean = false
    ) : PolygonMode
}

fun PolygonMode.apply() {
    val (mode, smooth) = when (this) {
        is PolygonMode.Fill -> GL_FILL to GL_POLYGON_SMOOTH

        is PolygonMode.Line -> {
            glLineWidth(width)
            GL_LINE to GL_LINE_SMOOTH
        }

        is PolygonMode.Point -> {
            glPointSize(size)
            GL_POINT to GL_POINT_SMOOTH
        }
    }

    if (antialiasing) {
        glEnable(smooth)
    } else {
        glDisable(smooth)
    }

    glPolygonMode(GL_FRONT_AND_BACK, mode)
}