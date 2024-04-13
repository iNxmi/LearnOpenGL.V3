package com.nami.world

import com.nami.Window
import com.nami.camera.Camera
import com.nami.constants.Directions
import com.nami.entity.Transform
import com.nami.input.Input
import com.nami.scene.SceneTime
import mu.KotlinLogging
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL
import org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE
import kotlin.math.cos
import kotlin.math.sin

class Player(val world: World) {

    companion object {
        @JvmStatic
        val speed = 3.0f

        @JvmStatic
        val sensivity = 0.11f
    }

    val log = KotlinLogging.logger { }

    val camera = Camera(90.0f, 16f / 9f, 0.001f, 1000.0f)
    val playerHeight = 1f

    val transform = Transform()
    val direction = Vector3f(0f, 0f, -1f)
    val acceleration = Vector3f(0f, 0f, 0f)

    fun update(time: SceneTime) {
        inputDirection()
        inputMovement(time)

        val chunkPos = Pair(
            (transform.position.x / Chunk.width.toFloat()).toInt(),
            (transform.position.z / Chunk.depth.toFloat()).toInt()
        )

        val chunk: Chunk? = world.chunks[chunkPos]

        var height = 0f
        if (chunk != null) {
            for (y in 1 until Chunk.height + 1) {
                val x = (transform.position.x % Chunk.width).toInt()
                val z = (transform.position.z % Chunk.depth).toInt()
                if (!chunk.blocks.containsKey(Vector3i(x, y, z))) {
                    height = y + playerHeight
                    break
                }
            }
        }

        val position = transform.position
        if (position.y > height)
            acceleration.add(0f, -16f * time.delta, 0f)

        if (Input.keyStates[GLFW_KEY_SPACE] == Input.State.DOWN)
            if (position.y <= height)
                acceleration.add(0f, 6.5f, 0f)

        position.add(Vector3f(acceleration).mul(time.delta))

        if (position.y < height) {
            position.y = height
            acceleration.y = 0f
        }

        camera.position.set(transform.position)
        camera.direction.set(direction)
    }

    private val eulerAngles = Vector3f()
    private fun inputDirection() {
        if (GLFW.glfwGetInputMode(Window.pointer, GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED) {
            eulerAngles.y += Input.posDelta.x * sensivity
            eulerAngles.x -= Input.posDelta.y * sensivity
            eulerAngles.x = eulerAngles.x.coerceIn(-80.9f, 89.9f)

            direction.set(
                cos(Math.toRadians(eulerAngles.y.toDouble())) * cos(Math.toRadians(eulerAngles.x.toDouble())),
                sin(Math.toRadians(eulerAngles.x.toDouble())),
                sin(Math.toRadians(eulerAngles.y.toDouble())) * cos(Math.toRadians(eulerAngles.x.toDouble()))
            ).normalize()
        }
    }

    private fun inputMovement(time: SceneTime) {
        val position = transform.position

        var speed = speed * time.delta
        if (Input.isKeyInStates(GLFW.GLFW_KEY_LEFT_SHIFT, Input.State.DOWN, Input.State.HOLD))
            speed *= 2f

        if (Input.isKeyInStates(GLFW.GLFW_KEY_W, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(direction).mul(1f, 0f, 1f).mul(speed))
        if (Input.isKeyInStates(GLFW.GLFW_KEY_S, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(direction).mul(1f, 0f, 1f).mul(-speed))

        if (Input.isKeyInStates(GLFW.GLFW_KEY_A, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(direction).cross(Directions.UP).normalize().mul(-speed))
        if (Input.isKeyInStates(GLFW.GLFW_KEY_D, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(direction).cross(Directions.UP).normalize().mul(speed))

        if (Input.isKeyInStates(GLFW.GLFW_KEY_SPACE, Input.State.DOWN, Input.State.HOLD))
            if (position.y <= 0f)
                acceleration.add(0f, 5f, 0f)

//        if (Input.isKeyInStates(GLFW_KEY_SPACE, Input.State.DOWN, Input.State.HOLD))
//            position.add(0f, speed, 0f)
//        if (Input.isKeyInStates(GLFW_KEY_LEFT_CONTROL, Input.State.DOWN, Input.State.HOLD))
//            position.add(0f, -speed, 0f)
    }

    fun render() {

    }

}