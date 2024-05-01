package com.nami.world.block

import org.joml.Vector4f

class BlockTemplate(
    val name: String,
    private val color: BlockColor,
    private val range: ClosedFloatingPointRange<Float>,
    val type: Block.Type
) {

    fun create(): Block {
        return Block(this, color.mutate(range))
    }

}

open class BlockColor(
    val cTop: Vector4f,
    val cBottom: Vector4f,
    val cLeft: Vector4f,
    val cRight: Vector4f,
    val cFront: Vector4f,
    val cBack: Vector4f
) {
    fun mutate(range: ClosedFloatingPointRange<Float>): BlockColor {
        val scalar: Float = (Math.random() * (1 - range.start) + range.endInclusive).toFloat()
        val max = Vector4f(1f, 1f, 1f, 1f)
        return BlockColor(
            Vector4f(cTop).mul(scalar, scalar, scalar, 1f).min(max),
            Vector4f(cBottom).mul(scalar, scalar, scalar, 1f).min(max),
            Vector4f(cLeft).mul(scalar, scalar, scalar, 1f).min(max),
            Vector4f(cRight).mul(scalar, scalar, scalar, 1f).min(max),
            Vector4f(cFront).mul(scalar, scalar, scalar, 1f).min(max),
            Vector4f(cBack).mul(scalar, scalar, scalar, 1f).min(max),
        )
    }
}

class BlockColorSingle(color: Vector4f) :
    BlockColor(Vector4f(color), Vector4f(color), Vector4f(color), Vector4f(color), Vector4f(color), Vector4f(color))

class BlockColorTop(top: Vector4f, other: Vector4f) :
    BlockColor(cTop = top, cBottom = other, cLeft = other, cRight = other, cFront = other, cBack = other)