package com.nami.world.block

import org.joml.Vector3f

class Block(val template: BlockTemplate, val color: BlockColor) {

    companion object {
        val invalid = BlockTemplate("Invalid", BlockColorSingle(Vector3f(1f, 0f, 1f)), (0.5f..1.0f))

        val bedrock = BlockTemplate("Bedrock", BlockColorSingle(Vector3f(1f, 1f, 1f).mul(0.25f)), (0.5f..0.75f))

        val water =
            BlockTemplate(
                "Water",
                BlockColorSingle(Vector3f(66f / 255f, 111f / 255f, 245f / 255f).mul(0.5f)),
                (0.5f..0.75f)
            )
        val sand = BlockTemplate("Sand", BlockColorSingle(Vector3f(0.625f, 0.625f, 0f)), (0.5f..1.0f))
        val stone = BlockTemplate("Stone", BlockColorSingle(Vector3f(0.5f, 0.5f, 0.5f).mul(0.75f)), (0.5f..1.0f))
        val dirt =
            BlockTemplate(
                "Dirt",
                BlockColorSingle(Vector3f(196f / 255f, 164f / 255f, 132f / 255f).mul(0.25f)),
                (0.5f..1.0f)
            )
        val grass = BlockTemplate(
            "Grass",
            BlockColorTop(Vector3f(0f, 0.6f, 0f).mul(0.5f), Vector3f(196f / 255f, 164f / 255f, 132f / 255f).mul(0.25f)),
            (0.5f..1.0f)
        )

        val snow = BlockTemplate("Snow", BlockColorSingle(Vector3f(1f, 1f, 1f).mul(0.85f)), (0.8f..1.0f))

        val log = BlockTemplate("Log", BlockColorSingle(Vector3f(71 / 255f, 54 / 255f, 21 / 255f)), (0.6f..1.0f))
        val leaves = BlockTemplate("Leaves", BlockColorSingle(Vector3f(0f, 1f, 0f).mul(0.5f)), (0.6f..1.0f))

        val cactus = BlockTemplate("Cactus", BlockColorSingle(Vector3f(0f, 1f, 0f).mul(0.25f)), (0.6f..1.0f))
    }

}

class BlockTemplate(
    val name: String,
    private val color: BlockColor,
    private val range: ClosedFloatingPointRange<Float>
) {

    fun create(): Block {
        return Block(this, color.mutate(range))
    }

}

open class BlockColor(
    val cTop: Vector3f,
    val cBottom: Vector3f,
    val cLeft: Vector3f,
    val cRight: Vector3f,
    val cFront: Vector3f,
    val cBack: Vector3f
) {
    fun mutate(range: ClosedFloatingPointRange<Float>): BlockColor {
        val scalar: Float = (Math.random() * (1 - range.start) + range.endInclusive).toFloat()
        val max = Vector3f(1f, 1f, 1f)
        return BlockColor(
            Vector3f(cTop).mul(scalar).min(max),
            Vector3f(cBottom).mul(scalar).min(max),
            Vector3f(cLeft).mul(scalar).min(max),
            Vector3f(cRight).mul(scalar).min(max),
            Vector3f(cFront).mul(scalar).min(max),
            Vector3f(cBack).mul(scalar).min(max),
        )
    }
}

class BlockColorSingle(color: Vector3f) :
    BlockColor(Vector3f(color), Vector3f(color), Vector3f(color), Vector3f(color), Vector3f(color), Vector3f(color))

class BlockColorTop(top: Vector3f, other: Vector3f) :
    BlockColor(cTop = top, cBottom = other, cLeft = other, cRight = other, cFront = other, cBack = other)