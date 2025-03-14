package com.nami.client.world.player

import com.nami.client.Directions
import com.nami.client.Transform
import com.nami.client.Window
import com.nami.client.camera.CameraPerspective
import com.nami.client.input.Keyboard
import com.nami.client.input.Mouse
import com.nami.client.resources.Resources
import com.nami.client.world.World
import com.nami.client.world.chunk.Chunk
import com.nami.client.world.resources.block.Block
import com.nami.client.world.resources.item.Item
import kotlinx.serialization.Serializable
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.glfw.GLFW.*
import kotlin.math.cos
import kotlin.math.sin

class Player(
    val world: World,
    val transform: Transform = Transform()
) {

    companion object {
        const val SPEED = 3.0f
        const val SENSITIVITY = 0.075f
        const val HEIGHT = 2.8f
        const val RANGE = 4
        const val MAX_ITERATIONS = RANGE * 16
    }

    private val blockManager = world.blockManager

    val camera = CameraPerspective(90.0f, 16.0f / 9.0f, 0.01f, 1023.0f)
    val acceleration = Vector3f(0f, 0f, 0f)

    val items = mutableMapOf<Item, Item.Instance>()
    var selectedItem = Resources.ITEM.get("tool.hand")

    init {
        items[Resources.ITEM.get("tool.hand")] = Resources.ITEM.get("tool.hand").create(count = 1)
        items[Resources.ITEM.get("lighter")] = Resources.ITEM.get("lighter").create(count = 1)
        items[Resources.ITEM.get("block.tnt")] = Resources.ITEM.get("block.tnt").create(count = 64)
    }

    fun update() {
        inputDirection()
        inputMovement()
    }

    fun getFacingBlock(): Block.Instance? {
        for (i in 0..MAX_ITERATIONS) {
            val pos = Vector3f(transform.position).add(0f, HEIGHT, 0f)
                .add(Vector3f(camera.directionFront).mul((i.toFloat() / MAX_ITERATIONS.toFloat()) * RANGE))
            val blockPos = Vector3i(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())

            val block = blockManager.getBlock(blockPos) ?: continue
            return block
        }

        return null
    }

    fun getPositionBeforeFacingBlock(): Vector3i? {
        for (i in 0..MAX_ITERATIONS) {
            val pos = Vector3f(transform.position).add(0f, HEIGHT, 0f)
                .add(Vector3f(camera.directionFront).mul((i.toFloat() / MAX_ITERATIONS.toFloat()) * RANGE))
            val blockPos = Vector3i(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())

            if (blockManager.getBlock(blockPos) == null)
                continue

            val lastPos =
                Vector3f(transform.position).add(0f, HEIGHT, 0f)
                    .add(Vector3f(camera.directionFront).mul(((i - 1).toFloat() / MAX_ITERATIONS.toFloat()) * RANGE))
            val lastBlockPos = Vector3i(lastPos.x.toInt(), lastPos.y.toInt(), lastPos.z.toInt())

            return lastBlockPos
        }

        return null
    }

    private val eulerAngles = Vector3f()
    private fun inputDirection() {
        if (glfwGetInputMode(Window.pointer, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
            eulerAngles.y += Mouse.posDelta.x * SENSITIVITY
            eulerAngles.x -= Mouse.posDelta.y * SENSITIVITY
            eulerAngles.x = eulerAngles.x.coerceIn(-89.9f, 89.9f)

            camera.directionFront.set(
                cos(Math.toRadians(eulerAngles.y.toDouble())) * cos(Math.toRadians(eulerAngles.x.toDouble())),
                sin(Math.toRadians(eulerAngles.x.toDouble())),
                sin(Math.toRadians(eulerAngles.y.toDouble())) * cos(Math.toRadians(eulerAngles.x.toDouble()))
            ).normalize()
        }
    }

    private fun inputMovement() {
        val position = transform.position

        var speed = SPEED * world.time.delta
        if (Keyboard.isKeyInStates(GLFW_KEY_LEFT_SHIFT, Keyboard.State.DOWN, Keyboard.State.HOLD))
            speed *= 2f

        val dir = Vector3f(camera.directionFront.x, 0f, camera.directionFront.z).normalize()
        val move = Vector3f()

        if (Keyboard.isKeyInStates(GLFW_KEY_W, Keyboard.State.DOWN, Keyboard.State.HOLD))
            move.add(Vector3f(dir).mul(1f, 0f, 1f))
        if (Keyboard.isKeyInStates(GLFW_KEY_S, Keyboard.State.DOWN, Keyboard.State.HOLD))
            move.add(Vector3f(dir).mul(1f, 0f, 1f).mul(-1f))

        if (Keyboard.isKeyInStates(GLFW_KEY_A, Keyboard.State.DOWN, Keyboard.State.HOLD))
            move.add(Vector3f(dir).cross(Directions.UP).normalize().mul(0.6f).mul(-1f))
        if (Keyboard.isKeyInStates(GLFW_KEY_D, Keyboard.State.DOWN, Keyboard.State.HOLD))
            move.add(Vector3f(dir).cross(Directions.UP).normalize().mul(0.6f))

        if (move.length() != 0f)
            position.add(Vector3f(move).normalize().mul(speed))

        val height =
            blockManager.getHeight(
                Vector2i(transform.position.x.toInt(), transform.position.z.toInt()),
                transform.position.y.toInt() + HEIGHT.toInt(),
                setOf(Block.Layer.SOLID, Block.Layer.FOLIAGE, Block.Layer.TRANSPARENT)
            ).toFloat()

        if (position.y > height)
            acceleration.add(0f, -21f * world.time.delta, 0f)

        if (Keyboard.isKeyInStates(GLFW_KEY_SPACE, Keyboard.State.DOWN, Keyboard.State.HOLD))
            if (position.y <= height)
                acceleration.add(0f, 7.5f, 0f)

        if (acceleration.y <= -100f)
            acceleration.y = -100f

        position.add(Vector3f(acceleration).mul(world.time.delta))

        position.x = position.x.coerceIn(0f, (world.size.x * Chunk.SIZE.x).toFloat() - 0.1f)
        position.z = position.z.coerceIn(0f, (world.size.z * Chunk.SIZE.z).toFloat() - 0.1f)

        if (position.y < height) {
            position.y = height
            acceleration.y = 0f
        }

        camera.transform.position.set(Vector3f(position).add(0f, HEIGHT, 0f))
    }

    @Serializable
    data class JSON(
        val transform: Transform.JSON
    ) {

        constructor(player: Player) : this(player.transform.json())

        fun create(world: World): Player {
            return Player(world, transform.create())
        }

    }

}