package com.nami.world.block

import org.joml.Vector4f

class Block(val template: BlockTemplate, val color: BlockColor) {

    companion object {
        val INVALID = BlockTemplate("Invalid", BlockColorSingle(Vector4f(1f, 0f, 1f, 1f)), (0.5f..1.0f), Type.SOLID)

        val BEDROCK =
            BlockTemplate("Bedrock", BlockColorSingle(Vector4f(0.25f, 0.25f, 0.25f, 1f)), (0.5f..0.75f), Type.SOLID)

        val WATER =
            BlockTemplate(
                "Water",
                BlockColorSingle(Vector4f(66f / 255f, 111f / 255f, 245f / 255f, 0.5f).mul(0.5f, 0.5f, 0.5f, 1.0f)),
                (0.5f..0.75f), Type.FLUID
            )
        val SAND = BlockTemplate("Sand", BlockColorSingle(Vector4f(0.5f, 0.5f, 0f, 1f)), (0.5f..1.0f), Type.SOLID)
        val STONE = BlockTemplate(
            "Stone",
            BlockColorSingle(Vector4f(0.5f, 0.5f, 0.5f, 1f).mul(0.75f, 0.75f, 0.75f, 1.0f)),
            (0.5f..1.0f), Type.SOLID
        )
        val DIRT =
            BlockTemplate(
                "Dirt",
                BlockColorSingle(Vector4f(196f / 255f, 164f / 255f, 132f / 255f, 1f).mul(0.25f, 0.25f, 0.25f, 1.0f)),
                (0.5f..1.0f), Type.SOLID
            )
        val GRASS = BlockTemplate(
            "Grass",
            BlockColorTop(
                Vector4f(0f, 0.6f, 0f, 1f).mul(0.5f, 0.5f, 0.5f, 1.0f),
                Vector4f(196f / 255f, 164f / 255f, 132f / 255f, 1f).mul(0.25f, 0.25f, 0.25f, 1.0f)
            ),
            (0.5f..1.0f), Type.SOLID
        )

        val MUSHROOM_STEM = BlockTemplate("Mushroom Stem", BlockColorSingle(Vector4f(200f/255f, 173f/255f, 127f/255f, 1f)), (0.75f..1.0f), Type.SOLID)
        val MUSHROOM_BLOCK_RED = BlockTemplate("Red Mushroom Block", BlockColorSingle(Vector4f(1f, 0f, 0f, 1f)), (0.75f..1.0f), Type.SOLID)
        val MYCELIUM = BlockTemplate("Mycelium", BlockColorSingle(Vector4f(0.5f, 0.5f, 0.5f, 1f)), (0.5f..1.0f), Type.SOLID)

        val SNOW = BlockTemplate("Snow", BlockColorSingle(Vector4f(0.85f, 0.85f, 0.85f, 1f)), (0.8f..1.0f), Type.SOLID)

        val LOG =
            BlockTemplate(
                "Log",
                BlockColorSingle(Vector4f(71 / 255f, 54 / 255f, 21 / 255f, 1f)),
                (0.6f..1.0f),
                Type.SOLID
            )
        val LEAVES = BlockTemplate("Leaves", BlockColorSingle(Vector4f(0f, 0.5f, 0f, 0.9f)), (0.6f..1.0f), Type.FOLIAGE)
        val LEAVES_SNOW = BlockTemplate("Snowy Leaves", BlockColorSingle(Vector4f(0.85f, 0.85f, 0.85f, 1f)), (0.8f..1.0f), Type.FOLIAGE)

        val CACTUS = BlockTemplate("Cactus", BlockColorSingle(Vector4f(0f, 0.25f, 0f, 1f)), (0.6f..1.0f), Type.SOLID)
    }

    enum class Type {
        SOLID,
        FOLIAGE,
        FLUID
    }

}