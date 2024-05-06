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
import kotlin.math.sin

class Player(val world: World) {

    companion object {
        const val SPEED = 3.0f
        const val SENSIVITY = 0.11f
        const val HEIGHT = 2.8f
        const val RANGE = 4f
        const val MAX_ITERATIONS = 512
    }

    val log = KotlinLogging.logger { }

    val blockManager = world.blockManager

    val camera = Camera(90.0f, 16.0f / 9.0f, 0.01f, 1023.0f)

    val transform = Transform()
    val acceleration = Vector3f(0f, 0f, 0f)

    val inventory = Inventory()
    var selectedBlock = Block.WATER

    fun update(time: SceneTime) {
        inputDirection()
        inputMovement(time)

        //Place
        if (Input.mouseButtonStates[GLFW_MOUSE_BUTTON_RIGHT] == Input.State.DOWN) {
            for (i in 0..MAX_ITERATIONS) {
                val pos =
                    Vector3f(transform.position).add(0f, HEIGHT, 0f)
                        .add(Vector3f(camera.directionFront).mul(i.toFloat() / MAX_ITERATIONS.toFloat() * RANGE))
                val blockPos = Vector3i(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())
                if (blockManager.getBlock(blockPos) != null) {
                    val lastPos =
                        Vector3f(transform.position).add(0f, HEIGHT, 0f)
                            .add(Vector3f(camera.directionFront).mul((i - 1).toFloat() / MAX_ITERATIONS.toFloat() * RANGE))
                    val lastBlockPos = Vector3i(lastPos.x.toInt(), lastPos.y.toInt(), lastPos.z.toInt())

                    val block = selectedBlock
                    inventory.remove(block, 1)
                    blockManager.setBlock(lastBlockPos, block)
                    break
                }
            }
        }

        //Break
        if (Input.mouseButtonStates[GLFW_MOUSE_BUTTON_LEFT] == Input.State.DOWN) {
            for (i in 0..MAX_ITERATIONS) {
                val pos =
                    Vector3f(transform.position).add(0f, HEIGHT, 0f)
                        .add(Vector3f(camera.directionFront).mul(i.toFloat() / MAX_ITERATIONS.toFloat() * RANGE))
                val blockPos = Vector3i(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())

                val block = blockManager.getBlock(blockPos) ?: continue

                inventory.add(block.template, 1)
                selectedBlock = block.template
                blockManager.setBlock(blockPos, null)
                break
            }
        }
    }

    private val eulerAngles = Vector3f()
    private fun inputDirection() {
        if (glfwGetInputMode(Window.pointer, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
            eulerAngles.y += Input.posDelta.x *  SENSIVITY
            eulerAngles.x -= Input.posDelta.y *  SENSIVITY
            eulerAngles.x = eulerAngles.x.coerceIn(-89.9f, 89.9f)

            camera.directionFront.set(
                cos(Math.toRadians(eulerAngles.y.toDouble())) * cos(Math.toRadians(eulerAngles.x.toDouble())),
                sin(Math.toRadians(eulerAngles.x.toDouble())),
                sin(Math.toRadians(eulerAngles.y.toDouble())) * cos(Math.toRadians(eulerAngles.x.toDouble()))
            ).normalize()
        }
    }

    private fun inputMovement(time: SceneTime) {
        val position = transform.position

        var speed = SPEED * time.delta
        if (Input.isKeyInStates(GLFW_KEY_LEFT_SHIFT, Input.State.DOWN, Input.State.HOLD))
            speed *= 2f

        val dir = Vector3f(camera.directionFront.x, 0f, camera.directionFront.z).normalize()

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
            blockManager.getHeight(
                Vector2i(transform.position.x.toInt(), transform.position.z.toInt()),
                transform.position.y.toInt() + HEIGHT.toInt()
            ).toFloat()

        if (position.y > height)
            acceleration.add(0f, -21f * time.delta, 0f)

        if (Input.isKeyInStates(GLFW_KEY_SPACE, Input.State.DOWN, Input.State.HOLD))
            if (position.y <= height)
                acceleration.add(0f, 7.5f, 0f)

        if (acceleration.y <= -100f)
            acceleration.y = -100f

        position.add(Vector3f(acceleration).mul(time.delta))

        position.x = position.x.coerceIn(0f, (World.SIZE.x * Chunk.SIZE.x).toFloat() - 0.1f)
        position.z = position.z.coerceIn(0f, (World.SIZE.z * Chunk.SIZE.z).toFloat() - 0.1f)

        if (position.y < height) {
            position.y = height
            acceleration.y = 0f
        }

        camera.transform.position.set(Vector3f(position).add(0f, HEIGHT, 0f))
    }

}