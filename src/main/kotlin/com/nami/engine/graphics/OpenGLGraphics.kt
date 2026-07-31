package com.nami.engine.graphics

import mu.KotlinLogging
import org.lwjgl.opengl.GL.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*
import org.lwjgl.system.MemoryStack.stackPush

class OpenGLGraphics : Graphics {

    private val logger = KotlinLogging.logger {}

    override val version: String
        get() = glGetString(GL_VERSION)?: ""

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