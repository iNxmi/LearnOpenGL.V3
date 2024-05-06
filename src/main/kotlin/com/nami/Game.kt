package com.nami

import com.nami.imgui.ImGUIManager
import com.nami.input.Input
import com.nami.resource.Resource
import com.nami.scene.SceneManager
import com.nami.scene.scenes.LoadingScene
import com.nami.scene.scenes.MainMenuScene
import com.nami.world.block.BlockColor
import mu.KotlinLogging
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_MULTISAMPLE
import java.util.concurrent.ConcurrentLinkedQueue

class Game {

    /*
    * Shadows
    * Particles
    * Audio
    * Clouds based on noise
    *
    * Fix visual artifacts when player is at very high distances away from (0,0) -> (100000, 100000)
    * Jitter problem fix -> https://www.reddit.com/r/Unity3D/comments/fv1rjm/terrain_and_objects_flickering_when_they_are_far/
        * The second problem that could be causing your issues is the limited precision of floating point values. The way floats work is that they only have a limited number of digits that they can store before they have to round up or down. A 32-bit float can typically store between 6-9 significant digits, this means that when you move far enough away from the origin (the point at (0,0,0)) anything doing math with floats begins to round values up and down.
        * Games with large open world get around this by moving the origin (google floating origin for more on this). One way to do this in Unity would be to move every object in the scene once the character, or camera, gets too far from (0,0,0)
    *
    * Filter huge lists -> https://stackoverflow.com/questions/77374173/what-is-the-best-way-to-filter-big-lists-in-kotlin
    *
    * */

    companion object {
        var DELTA_TIME = 0F

        val QUEUE = ConcurrentLinkedQueue<Runnable>()
    }

    private val log = KotlinLogging.logger {}

    init {
        log.info("LWJGL Version: ${Version.getVersion()}")
        log.info("GLFW Version: ${glfwGetVersionString()}")

        Window.init(1920, 1080)

        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        glEnable(GL_MULTISAMPLE)

        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        SceneManager.selected = LoadingScene({
            Resource.shader.load()
            BlockColor.generateTexture()
        }, MainMenuScene())

        glfwShowWindow(Window.pointer)

        loop()

        ImGUIManager.delete()

        glfwFreeCallbacks(Window.pointer)
        glfwDestroyWindow(Window.pointer)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun loop() {
        var lastTime = 0f
        while (!glfwWindowShouldClose(Window.pointer)) {
            DELTA_TIME = glfwGetTime().toFloat() - lastTime
            lastTime = glfwGetTime().toFloat()

            for(i in 0 until 3) {
                if(QUEUE.isNotEmpty())
                    QUEUE.remove().run()
            }
            Input.update()

            SceneManager.update()

            render()

            Input.endFrame()
            glfwPollEvents()
        }
    }

    private fun render() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

        SceneManager.render()
        SceneManager.renderHUD()

        val error = glGetError()
        if (error != 0)
            log.warn { "OpenGL Error: $error" }

        glfwSwapBuffers(Window.pointer)
    }

}

fun main() {
    Game()
}