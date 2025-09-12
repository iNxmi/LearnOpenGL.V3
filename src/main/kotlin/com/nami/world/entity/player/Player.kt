package com.nami.world.entity.player

import com.nami.Directions
import com.nami.Transform
import com.nami.Window
import com.nami.camera.CameraPerspective
import com.nami.resources.Resources
import com.nami.world.World
import com.nami.world.chunk.Chunk
import com.nami.world.entity.Entity
import com.nami.world.resources.block.Block
import com.nami.world.resources.item.Item
import kotlinx.serialization.Serializable
import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.glfw.GLFW.*
import kotlin.math.cos
import kotlin.math.sin

class Player(
    val world: World,
    val transform: Transform = Transform()
) : Entity() {

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
    var selectedItem = Resources.ITEM.get("tool.hand").create(count = 1)

    init {
        items[Resources.ITEM.get("tool.hand")] = Resources.ITEM.get("tool.hand").create(count = 1)
        items[Resources.ITEM.get("lighter")] = Resources.ITEM.get("lighter").create(count = 1)
        items[Resources.ITEM.get("block.tnt")] = Resources.ITEM.get("block.tnt").create(count = 64)
    }

    fun onKeyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        val position = transform.position

        var speed = SPEED * world.time.delta
        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS)
            speed *= 2f

        val directionView = Vector3f(camera.directionFront.x, 0f, camera.directionFront.z).normalize()
        val directionMove = Vector3f()

        if (key == GLFW_KEY_W && (action == GLFW_PRESS || action == GLFW_REPEAT))
            directionMove.add(Vector3f(directionView).mul(1f, 0f, 1f))
        if (key == GLFW_KEY_S && (action == GLFW_PRESS || action == GLFW_REPEAT))
            directionMove.add(Vector3f(directionView).mul(1f, 0f, 1f).mul(-1f))
        if (key == GLFW_KEY_A && (action == GLFW_PRESS || action == GLFW_REPEAT))
            directionMove.add(Vector3f(directionView).cross(Directions.UP.vector).normalize().mul(0.6f).mul(-1f))
        if (key == GLFW_KEY_D && (action == GLFW_PRESS || action == GLFW_REPEAT))
            directionMove.add(Vector3f(directionView).cross(Directions.UP.vector).normalize().mul(0.6f))

        if (directionMove.length() != 0f)
            position.add(Vector3f(directionMove).normalize().mul(speed))

        if (key == GLFW_KEY_SPACE && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            val position = transform.position
            if (position.y <= getGroundHeight())
                acceleration.add(0f, 7.5f, 0f)
        }
    }

    fun onMouseButtonCallback(window: Long, button: Int, action: Int, mods: Int) {
        //Primary
        if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            val handler = selectedItem.handler
            val consumed = handler.onPrimaryUse(selectedItem, this)
        }

        //Secondary
        if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
            val handler = selectedItem.handler
            val consumed = handler.onSecondaryUse(selectedItem, this)
        }
    }

    private val eulerAngles = Vector3f()
    private val cursorPositionLast = Vector2f()
    private var first = true
    fun onCursorPosCallback(window: Long, x: Double, y: Double) {
        val cursorPosition = Vector2f(x.toFloat(), y.toFloat())
        if (first) {
            cursorPositionLast.set(cursorPosition)
            first = false
            return
        }

        val cursorPositionDelta = Vector2f(cursorPosition).sub(cursorPositionLast)

        if (glfwGetInputMode(Window.pointer, GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
            eulerAngles.y += cursorPositionDelta.x * SENSITIVITY
            eulerAngles.x -= cursorPositionDelta.y * SENSITIVITY
            eulerAngles.x = eulerAngles.x.coerceIn(-89.9f, 89.9f)

            camera.directionFront.set(
                cos(Math.toRadians(eulerAngles.y.toDouble())) * cos(Math.toRadians(eulerAngles.x.toDouble())),
                sin(Math.toRadians(eulerAngles.x.toDouble())),
                sin(Math.toRadians(eulerAngles.y.toDouble())) * cos(Math.toRadians(eulerAngles.x.toDouble()))
            ).normalize()
        }

        cursorPositionLast.set(cursorPosition)
    }

    fun update() {
        if (acceleration.y <= -100f)
            acceleration.y = -100f

        val position = transform.position

        position.add(Vector3f(acceleration).mul(world.time.delta))

        position.x = position.x.coerceIn(0f, (world.size.x * Chunk.SIZE.x).toFloat() - 0.1f)
        position.z = position.z.coerceIn(0f, (world.size.z * Chunk.SIZE.z).toFloat() - 0.1f)

        val height = getGroundHeight()
        if (position.y > height)
            acceleration.add(0f, -21f * world.time.delta, 0f)

        if (position.y < height) {
            position.y = height
            acceleration.y = 0f
        }

        camera.transform.position.set(Vector3f(position).add(0f, HEIGHT, 0f))
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

    fun getGroundHeight() = getGroundHeight(
        Vector3i(
            transform.position.x.toInt(),
            transform.position.y.toInt(),
            transform.position.z.toInt()
        )
    )

    fun getGroundHeight(position: Vector3i) = blockManager.getHeight(
        Vector2i(position.x, position.z),
        position.y + HEIGHT.toInt(),
        setOf(Block.Layer.SOLID, Block.Layer.FOLIAGE, Block.Layer.TRANSPARENT)
    ).toFloat()

    @Serializable
    data class JSON(
        val transform: Transform.JSON
    ) {
        constructor(player: Player) : this(player.transform.json())

        fun create(world: World) = Player(world, transform.create())
    }

}