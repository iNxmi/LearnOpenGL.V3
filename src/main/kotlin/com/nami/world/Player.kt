package com.nami.world

import com.nami.Window
import com.nami.camera.Camera
import com.nami.constants.Directions
import com.nami.entity.Transform
import com.nami.input.Input
import com.nami.scene.SceneTime
import com.nami.world.block.Block
import com.nami.world.chunk.Chunk
import mu.KotlinLogging
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.glfw.GLFW.*
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class Player(val world: World) {

    companion object {
        @JvmStatic
        val speed = 3.0f

        @JvmStatic
        val sensivity = 0.15f

        @JvmStatic
        val height = 2.8f
    }

    val log = KotlinLogging.logger { }

    val camera = Camera(90.0f, 16.0f / 9.0f, 0.01f, 1023.0f)
    val playerHeight = 1f

    val transform = Transform()
    val direction = Vector3f(0f, 0f, -1f)
    val acceleration = Vector3f(0f, 0f, 0f)

    val range = 4f
    val maxIterations = 512

    val inventory = Inventory()

    fun update(time: SceneTime) {
        inputDirection()
        inputMovement(time)

        //Place
        if (Input.mouseButtonStates[GLFW_MOUSE_BUTTON_RIGHT] == Input.State.DOWN) {
            for (i in 0..maxIterations) {
                val pos =
                    Vector3f(transform.position).add(0f, height, 0f)
                        .add(Vector3f(direction).mul(i.toFloat() / maxIterations.toFloat() * range))
                val blockPos = Vector3i(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())
                if (world.getBlock(blockPos) != null) {
                    val lastPos =
                        Vector3f(transform.position).add(0f, height, 0f).add(Vector3f(direction).mul((i - 1).toFloat() / maxIterations.toFloat() * range))
                    val lastBlockPos = Vector3i(lastPos.x.toInt(), lastPos.y.toInt(), lastPos.z.toInt())

                    val block = Block.invalid
                    inventory.remove(block, 1)
                    world.setBlock(lastBlockPos, block.create())
                    break
                }
            }
        }

        //Break
        if (Input.mouseButtonStates[GLFW_MOUSE_BUTTON_LEFT] == Input.State.DOWN) {
            for (i in 0..maxIterations) {
                val pos =
                    Vector3f(transform.position).add(0f, height, 0f).add(Vector3f(direction).mul(i.toFloat() / maxIterations.toFloat() * range))
                val blockPos = Vector3i(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())

                val block = world.getBlock(blockPos) ?: continue

                inventory.add(block.template, 1)
                world.setBlock(blockPos, null)
                break
            }
        }
    }

    private val eulerAngles = Vector3f()
    private fun inputDirection() {
        if (glfwGetInputMode(Window.pointer, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
            eulerAngles.y += Input.posDelta.x * sensivity
            eulerAngles.x -= Input.posDelta.y * sensivity
            eulerAngles.x = eulerAngles.x.coerceIn(-89.9f, 89.9f)

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
        if (Input.isKeyInStates(GLFW_KEY_LEFT_SHIFT, Input.State.DOWN, Input.State.HOLD))
            speed *= 2f

        val dir = Vector3f(direction.x, 0f, direction.z).normalize()

        if (Input.isKeyInStates(GLFW_KEY_W, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(dir).mul(1f, 0f, 1f).mul(speed))
        if (Input.isKeyInStates(GLFW_KEY_S, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(dir).mul(1f, 0f, 1f).mul(-speed))

        if (Input.isKeyInStates(GLFW_KEY_A, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(dir).cross(Directions.UP).normalize().mul(-speed))
        if (Input.isKeyInStates(GLFW_KEY_D, Input.State.DOWN, Input.State.HOLD))
            position.add(Vector3f(dir).cross(Directions.UP).normalize().mul(speed))

        if (Input.isKeyInStates(GLFW_KEY_SPACE, Input.State.DOWN, Input.State.HOLD))
            if (position.y <= 0f)
                acceleration.add(0f, 5f, 0f)

        val height =
            world.getHeight(
                Vector2i(transform.position.x.toInt(), transform.position.z.toInt()),
                transform.position.y.toInt() + Player.height.toInt()
            ).toFloat()

        if (position.y > height)
            acceleration.add(0f, -21f * time.delta, 0f)

        if (Input.isKeyInStates(GLFW_KEY_SPACE, Input.State.DOWN, Input.State.HOLD))
            if (position.y <= height)
                acceleration.add(0f, 7.5f, 0f)

        if (acceleration.y <= -100f)
            acceleration.y = -100f

        position.add(Vector3f(acceleration).mul(time.delta))

        position.x = position.x.coerceIn(0f, (World.width * Chunk.width).toFloat() - 0.1f)
        position.z = position.z.coerceIn(0f, (World.depth * Chunk.depth).toFloat() - 0.1f)

        if (position.y < height) {
            position.y = height
            acceleration.y = 0f
        }

        camera.position.set(Vector3f(position).add(0f, Player.height, 0f))
        camera.direction.set(direction)
    }

}