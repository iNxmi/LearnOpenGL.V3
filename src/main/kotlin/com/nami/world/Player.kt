package com.nami.world

import com.nami.Window
import com.nami.camera.Camera
import com.nami.constants.Directions
import com.nami.entity.Transform
import com.nami.input.Input
import com.nami.scene.SceneTime
import mu.KotlinLogging
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Player(val world: World) {

    companion object {
        @JvmStatic
        val speed = 3.0f

        @JvmStatic
        val sensivity = 0.11f
    }

    val log = KotlinLogging.logger { }

    val camera = Camera(90.0f, 16.0f / 9.0f, 0.01f, 1023.0f)
    val playerHeight = 1f

    val transform = Transform()
    val direction = Vector3f(0f, 0f, -1f)
    val acceleration = Vector3f(0f, 0f, 0f)

    val range = 4f
    val maxIterations = 512


    fun update(time: SceneTime) {
        inputDirection()
        inputMovement(time)

        //Place
        if (Input.mouseButtonStates[GLFW_MOUSE_BUTTON_RIGHT] == Input.State.DOWN) {
            for (i in 0..maxIterations) {
                val pos =
                    Vector3f(transform.position).add(Vector3f(direction).mul(i.toFloat() / maxIterations.toFloat() * range))
                val blockPos = Vector3i(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())
                if (world.getBlock(blockPos) != null) {
                    val lastPos =
                        Vector3f(transform.position).add(Vector3f(direction).mul((i - 1).toFloat() / maxIterations.toFloat() * range))
                    val lastBlockPos = Vector3i(lastPos.x.toInt(), lastPos.y.toInt(), lastPos.z.toInt())
                    world.setBlock(
                        lastBlockPos,
                        BlockOneColor(Vector3f(1f, 0f, 0f).mul((Math.random() * (1.0 - 0.5) + 0.5).toFloat()))
                    )
                    break
                }
            }
        }

        //Break
        if (Input.mouseButtonStates[GLFW_MOUSE_BUTTON_LEFT] == Input.State.DOWN) {
            for (i in 0..maxIterations) {
                val pos =
                    Vector3f(transform.position).add(Vector3f(direction).mul(i.toFloat() / maxIterations.toFloat() * range))
                val blockPos = Vector3i(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())
                if (world.getBlock(blockPos) != null) {
                    world.setBlock(blockPos, null)
                    break
                }
            }
        }
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

        val dir = Vector3f(direction.x, 0f, direction.z).normalize()

        if (Input.isKeyInStates(GLFW.GLFW_KEY_W, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(dir).mul(1f, 0f, 1f).mul(speed))
        if (Input.isKeyInStates(GLFW.GLFW_KEY_S, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(dir).mul(1f, 0f, 1f).mul(-speed))

        if (Input.isKeyInStates(GLFW.GLFW_KEY_A, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(dir).cross(Directions.UP).normalize().mul(-speed))
        if (Input.isKeyInStates(GLFW.GLFW_KEY_D, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(dir).cross(Directions.UP).normalize().mul(speed))

        if (Input.isKeyInStates(GLFW_KEY_SPACE, Input.State.DOWN, Input.State.HOLD))
            if (position.y <= 0f)
                acceleration.add(0f, 5f, 0f)

//        if (Input.isKeyInStates(GLFW_KEY_SPACE, Input.State.DOWN, Input.State.HOLD))
//            position.add(0f, speed, 0f)
//        if (Input.isKeyInStates(GLFW_KEY_LEFT_CONTROL, Input.State.DOWN, Input.State.HOLD))
//            position.add(0f, -speed, 0f)

        val height =
            world.getHeight(Vector2i(transform.position.x.toInt(), transform.position.z.toInt())).toFloat() + 1 + 1.5f

        if (position.y > height)
            acceleration.add(0f, -21f * time.delta, 0f)

        if (Input.isKeyInStates(GLFW_KEY_SPACE, Input.State.DOWN, Input.State.HOLD))
            if (position.y <= height)
                acceleration.add(0f, 7.5f, 0f)

        if (acceleration.y <= -100f)
            acceleration.y = -100f

        position.add(Vector3f(acceleration).mul(time.delta))

        if (position.y < height) {
            position.y = height
            acceleration.y = 0f
        }

        camera.position.set(transform.position)
        camera.direction.set(direction)
    }

}