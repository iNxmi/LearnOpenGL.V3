package com.nami.engine.graphics

import mu.KotlinLogging
import org.lwjgl.opengl.GL.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.system.MemoryStack.stackPush

class OpenGLGraphics : Graphics {

    private val logger = KotlinLogging.logger {}

    override val version: String
        get() = glGetString(GL_VERSION)?: "Error"

    override var viewport: Area
        get() = stackPush().use{ stack ->
            val buffer = stack.mallocInt(4)
            glGetIntegerv(GL_VIEWPORT, buffer)

            return Area(
                x = buffer[0],
                y = buffer[1],
                width = buffer[2],
                height = buffer[3]
            )
        }
        set(value) = glViewport(value.x, value.y, value. width, value.height)

    override var polygonMode: PolygonMode = PolygonMode.Fill()
        get() {
            val code = glGetInteger(GL_POLYGON_MODE)
            return when(code) {
                GL_FILL -> {
                    val antialiasing = glIsEnabled(GL_POLYGON_SMOOTH)
                    PolygonMode.Fill(antialiasing)
                }
                GL_LINE -> {
                    val antialiasing = glIsEnabled(GL_LINE_SMOOTH)
                    val width = glGetFloat(GL_LINE_WIDTH)
                    PolygonMode.Line(width, antialiasing)
                }
                GL_POINT -> {
                    val antialiasing = glIsEnabled(GL_POINT_SMOOTH)
                    val size = glGetFloat(GL_POINT_SIZE)
                    PolygonMode.Point(size, antialiasing)
                }
                else -> throw IllegalArgumentException()
            }
        }
        set(value) {
            value.apply()
            field = value
        }

    override var cullingMode: CullingMode
        get() {
            val code = glGetInteger(GL_CULL_FACE)
            return CullingMode.Mapper.getOpenGL(code)
        }
        set(value) = if(value == CullingMode.DISABLED) {
            glDisable(GL_CULL_FACE)
        } else {
            glEnable(GL_CULL_FACE)
            glCullFace(value.openglCode)
        }

    override fun initialize() {
        createCapabilities()

        logger.info{ "OpenGL Version: $version" }

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_MULTISAMPLE)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    }

    override fun clear() = glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

    override fun getErrorCode() = glGetError()

}