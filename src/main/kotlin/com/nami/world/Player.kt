package com.nami.world

import com.nami.Directions
import com.nami.Input
import com.nami.Transform
import com.nami.Window
import com.nami.camera.CameraPerspective
import com.nami.world.block.Block
import com.nami.world.block.Layer
import com.nami.world.chunk.Chunk
import com.nami.world.item.Item
import com.nami.world.item.items.ItemAcorn
import com.nami.world.item.items.ItemLighter
import com.nami.world.item.items.ItemTnt
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.glfw.GLFW
import kotlin.math.cos
import kotlin.math.sin

class Player {

    companion object {
        const val SPEED = 3.0f
        const val SENSITIVITY = 0.075f
        const val HEIGHT = 2.8f
        const val RANGE = 4
        const val MAX_ITERATIONS = RANGE * 16
    }

    val transform = Transform()

    val camera = CameraPerspective(90.0f, 16.0f / 9.0f, 0.01f, 1024.0f)

    val acceleration = Vector3f(0f, 0f, 0f)

    val items = mutableMapOf(
        ItemLighter to 1,
        ItemTnt to 64,
        ItemAcorn to 64
    )

    var selectedItem: Item? = null

    fun update(world: World) {
        inputDirection()
        inputMovement(world)
        inputAction(world)
    }

    private val eulerAngles = Vector3f()

    private val mousePositionLast = Vector2i()

    private var first = true
    private fun inputDirection() {
        val mousePosition = Input.position()
        if (first) {
            mousePositionLast.set(mousePosition)
            first = false
            return
        }

        val mousePositionDelta = Vector2i(mousePosition).sub(mousePositionLast)

        if (GLFW.glfwGetInputMode(Window.pointer, GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED) {
            eulerAngles.y += mousePositionDelta.x * SENSITIVITY
            eulerAngles.x -= mousePositionDelta.y * SENSITIVITY
            eulerAngles.x = eulerAngles.x.coerceIn(-89.9f, 89.9f)

            camera.directionFront.set(
                cos(Math.toRadians(eulerAngles.y.toDouble())) * cos(Math.toRadians(eulerAngles.x.toDouble())),
                sin(Math.toRadians(eulerAngles.x.toDouble())),
                sin(Math.toRadians(eulerAngles.y.toDouble())) * cos(Math.toRadians(eulerAngles.x.toDouble()))
            ).normalize()
        }

        mousePositionLast.set(mousePosition)
    }

    private fun inputMovement(world: World) {
        val position = transform.position

        var speed = SPEED * world.time.delta
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT))
            speed *= 2f

        val dir = Vector3f(camera.directionFront.x, 0f, camera.directionFront.z).normalize()
        val move = Vector3f()

        if (Input.isKeyDown(GLFW.GLFW_KEY_W))
            move.add(Vector3f(dir).mul(1f, 0f, 1f))
        if (Input.isKeyDown(GLFW.GLFW_KEY_S))
            move.add(Vector3f(dir).mul(1f, 0f, 1f).mul(-1f))

        if (Input.isKeyDown(GLFW.GLFW_KEY_A))
            move.add(Vector3f(dir).cross(Directions.UP.vector).normalize().mul(0.6f).mul(-1f))
        if (Input.isKeyDown(GLFW.GLFW_KEY_D))
            move.add(Vector3f(dir).cross(Directions.UP.vector).normalize().mul(0.6f))

        if (move.length() != 0f)
            position.add(Vector3f(move).normalize().mul(speed))


        val blockManager = world.blockManager

//        val height = blockManager.getHeight(
//            Vector2i(transform.position.x.toInt(), transform.position.z.toInt()),
//            transform.position.y.toInt() + HEIGHT.toInt(),
//            setOf(Layer.SOLID, Layer.FOLIAGE, Layer.TRANSPARENT)
//        ).toFloat()

        val height = 120f

        if (position.y > height)
            acceleration.add(0f, -21f * world.time.delta, 0f)

        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE))
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

    fun inputAction(world: World) {
        //Primary
        if (Input.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_LEFT))
            selectedItem?.onPrimaryUse()

        //Secondary
        if (Input.isMousePressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT))
            selectedItem?.onSecondaryUse()
    }

    fun getFacingMaterial(world: World): Block? {
        for (i in 0..MAX_ITERATIONS) {
            val pos = Vector3f(transform.position).add(0f, HEIGHT, 0f)
                .add(Vector3f(camera.directionFront).mul((i.toFloat() / MAX_ITERATIONS.toFloat()) * RANGE))
            val blockPos = Vector3i(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())


            val blockManager = world.blockManager
            val block = blockManager.getBlock(blockPos) ?: continue
            return block
        }

        return null
    }

    fun getPositionBeforeFacingBlock(world: World): Vector3i? {
        for (i in 0..MAX_ITERATIONS) {
            val pos = Vector3f(transform.position).add(0f, HEIGHT, 0f)
                .add(Vector3f(camera.directionFront).mul((i.toFloat() / MAX_ITERATIONS.toFloat()) * RANGE))
            val blockPos = Vector3i(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())

            val blockManager = world.blockManager
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

    fun getGroundHeight(world: World) = getGroundHeight(
        world,
        Vector3i(
            transform.position.x.toInt(),
            transform.position.y.toInt(),
            transform.position.z.toInt()
        )
    )

    fun getGroundHeight(world: World, position: Vector3i): Float {
        val blockManager = world.blockManager
        return blockManager.getHeight(
            Vector2i(position.x, position.z),
            position.y + HEIGHT.toInt(),
            setOf(Layer.SOLID, Layer.FOLIAGE, Layer.TRANSPARENT)
        ).toFloat()
    }

}