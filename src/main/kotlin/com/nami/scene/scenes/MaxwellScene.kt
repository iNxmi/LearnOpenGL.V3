package com.nami.scene.scenes

import com.nami.camera.Camera
import com.nami.Game
import com.nami.input.Input
import com.nami.model.ModelManager
import com.nami.scene.Scene
import com.nami.shader.ShaderManager
import imgui.ImGui
import imgui.flag.ImGuiWindowFlags
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.opengl.GL33.*
import kotlin.math.pow

class MaxwellScene : Scene {

    private val maxwell = ModelManager.get("maxwell")
    private val camera = Camera(Math.toRadians(20.0).toFloat(), 16f / 9f, 0.001f, 100.0f)

    private var menu = false

    private val position = floatArrayOf(0f, 0f, 0f)

    private val rpm = floatArrayOf(0f, -15.0f, 0f)
    private val angles = floatArrayOf(0f, 0f, 0f)

    private val scale = floatArrayOf(1f, 1f, 1f)

    init {
        glClearColor(1f, 1f, 1f, 1f)
        camera.position.set(0f, 1f, 3f)
        camera.direction.set(Vector3f(0f, 0.32f, 0f).sub(camera.position).normalize())
    }

    override fun update() {
        if (Input.keyStates[GLFW_KEY_ESCAPE] == Input.State.DOWN)
            menu = !menu

        angles[0] += rpm[0] / 60 * 360 * Game.DELTA_TIME
        angles[1] += rpm[1] / 60 * 360 * Game.DELTA_TIME
        angles[2] += rpm[2] / 60 * 360 * Game.DELTA_TIME
    }

    override fun render() {
        val shader = ShaderManager.bind("maxwell")

        val model = Matrix4f()
            .translate(position[0], position[1], position[2])
            .rotateX(Math.toRadians(angles[0].toDouble()).toFloat())
            .rotateY(Math.toRadians(angles[1].toDouble()).toFloat())
            .rotateZ(Math.toRadians(angles[2].toDouble()).toFloat())
            .scale(scale[0], scale[1], scale[2])
        shader.uniform.set("u_model", model).set("u_projection", camera.projection).set("u_view", camera.view)

        maxwell.render(shader)

        ShaderManager.unbind()
    }

    override fun renderNVG() {

    }

    override fun renderImGUI() {
        if (!menu)
            return

        val width = IntArray(1)
        val height = IntArray(1)
        GLFW.glfwGetWindowSize(Game.WINDOW_PTR, width, height)

        ImGui.getFont().scale = 2f
        ImGui.setNextWindowPos(0f, 0f)
        ImGui.setNextWindowSize(width[0] / 2f, height[0].toFloat())

        ImGui.begin("Maxwell Settings", ImGuiWindowFlags.NoDecoration or ImGuiWindowFlags.NoMove)

        if (ImGui.collapsingHeader("Debug")) {
            ImGui.text("FPS=${(1f / Game.DELTA_TIME).round(2)}")
            ImGui.text("DELTA_TIME=${Game.DELTA_TIME}")
            ImGui.text("glfwGetTime=${glfwGetTime().toFloat().round(2)}")
        }

        if (ImGui.collapsingHeader("Camera")) {

        }

        if (ImGui.collapsingHeader("Transform")) {
            ImGui.text("Position")
            if (ImGui.button("Reset a")) {
                position[0] = 0f
                position[1] = 0f
                position[2] = 0f
            }
            ImGui.sameLine()
            ImGui.dragFloat3("Position", position, 0.25f)


            ImGui.text("Rotation")
            if (ImGui.button("Reset b")) {
                rpm[0] = 0f
                rpm[1] = 0f
                rpm[2] = 0f
            }
            ImGui.sameLine()
            ImGui.dragFloat3("Spin (rpm)", rpm, 0.25f)

            if (ImGui.button("Reset c")) {
                angles[0] = 0f
                angles[1] = 0f
                angles[2] = 0f
            }
            ImGui.sameLine()
            ImGui.dragFloat3("Angels (deg)", angles, 0.25f)


            ImGui.text("Scale")
            if (ImGui.button("Reset d")) {
                scale[0] = 1f
                scale[1] = 1f
                scale[2] = 1f
            }
            ImGui.sameLine()
            ImGui.dragFloat3("Scale", scale, 0.1f)
        }

        ImGui.end()
    }

    private fun Float.round(decimals: Int): Float {
        val mul: Float = (10.0).pow(decimals.toDouble()).toFloat()
        return kotlin.math.round(this * mul) / mul
    }

}