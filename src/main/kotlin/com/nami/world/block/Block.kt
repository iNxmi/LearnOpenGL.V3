package com.nami.world.block

import de.articdive.jnoise.core.api.functions.Interpolation
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction
import de.articdive.jnoise.pipeline.JNoise
import org.joml.Vector3i
import kotlin.math.roundToInt

class Block(val template: Template, val mutation: Float) {

    companion object {
        val INVALID = Template(
            "Invalid",
            BlockColor.INVALID,
            BlockColor.INVALID,
            BlockColor.INVALID,
            BlockColor.INVALID,
            BlockColor.INVALID,
            BlockColor.INVALID,
            Type.SOLID
        )

        val BEDROCK =
            Template(
                "Bedrock",
                BlockColor.INVALID,
                BlockColor.BEDROCK,
                BlockColor.BEDROCK,
                BlockColor.BEDROCK,
                BlockColor.BEDROCK,
                BlockColor.BEDROCK,
                Type.SOLID
            )

        val WATER =
            Template(
                "Water",
                BlockColor.WATER,
                BlockColor.WATER,
                BlockColor.WATER,
                BlockColor.WATER,
                BlockColor.WATER,
                BlockColor.WATER,
                Type.FLUID
            )

        val SAND = Template(
            "Sand",
            BlockColor.SAND,
            BlockColor.SAND,
            BlockColor.SAND,
            BlockColor.SAND,
            BlockColor.SAND,
            BlockColor.SAND,
            Type.SOLID
        )
        val STONE = Template(
            "Stone",
            BlockColor.STONE,
            BlockColor.STONE,
            BlockColor.STONE,
            BlockColor.STONE,
            BlockColor.STONE,
            BlockColor.STONE,
            Type.SOLID
        )

        val DIRT =
            Template(
                "Dirt",
                BlockColor.DIRT,
                BlockColor.DIRT,
                BlockColor.DIRT,
                BlockColor.DIRT,
                BlockColor.DIRT,
                BlockColor.DIRT,
                Type.SOLID
            )

        val GRASS = Template(
            "Grass",
            BlockColor.GRASS,
            BlockColor.DIRT,
            BlockColor.DIRT,
            BlockColor.DIRT,
            BlockColor.DIRT,
            BlockColor.DIRT,
            Type.SOLID
        )

        val MUSHROOM_STEM = Template(
            "Mushroom Stem",
            BlockColor.MUSHROOM_STEM,
            BlockColor.MUSHROOM_STEM,
            BlockColor.MUSHROOM_STEM,
            BlockColor.MUSHROOM_STEM,
            BlockColor.MUSHROOM_STEM,
            BlockColor.MUSHROOM_STEM,
            Type.SOLID
        )
        val MUSHROOM_BLOCK_RED = Template(
            "Red Mushroom Block",
            BlockColor.MUSHROOM_BLOCK_RED,
            BlockColor.MUSHROOM_BLOCK_RED,
            BlockColor.MUSHROOM_BLOCK_RED,
            BlockColor.MUSHROOM_BLOCK_RED,
            BlockColor.MUSHROOM_BLOCK_RED,
            BlockColor.MUSHROOM_BLOCK_RED,
            Type.SOLID
        )
        val MYCELIUM =
            Template(
                "Mycelium",
                BlockColor.MYCELIUM,
                BlockColor.MYCELIUM,
                BlockColor.MYCELIUM,
                BlockColor.MYCELIUM,
                BlockColor.MYCELIUM,
                BlockColor.MYCELIUM,
                Type.SOLID
            )

        val SNOW = Template(
            "Snow",
            BlockColor.SNOW,
            BlockColor.SNOW,
            BlockColor.SNOW,
            BlockColor.SNOW,
            BlockColor.SNOW,
            BlockColor.SNOW,
            Type.SOLID
        )

        val LOG =
            Template(
                "Log",
                BlockColor.LOG,
                BlockColor.LOG,
                BlockColor.LOG,
                BlockColor.LOG,
                BlockColor.LOG,
                BlockColor.LOG,
                Type.SOLID
            )
        val LEAVES =
            Template(
                "Leaves",
                BlockColor.LEAVES,
                BlockColor.LEAVES,
                BlockColor.LEAVES,
                BlockColor.LEAVES,
                BlockColor.LEAVES,
                BlockColor.LEAVES,
                Type.FOLIAGE
            )
        val LEAVES_SNOW = Template(
            "Snowy Leaves",
            BlockColor.LEAVES_SNOW,
            BlockColor.LEAVES_SNOW,
            BlockColor.LEAVES_SNOW,
            BlockColor.LEAVES_SNOW,
            BlockColor.LEAVES_SNOW,
            BlockColor.LEAVES_SNOW,
            Type.FOLIAGE
        )

        val CACTUS = Template(
            "Cactus",
            BlockColor.CACTUS,
            BlockColor.CACTUS,
            BlockColor.CACTUS,
            BlockColor.CACTUS,
            BlockColor.CACTUS,
            BlockColor.CACTUS,
            Type.SOLID
        )
    }

    enum class Type {
        SOLID,
        FOLIAGE,
        FLUID
    }

    class Template(
        val name: String,
        val colorTop: BlockColor,
        val colorBottom: BlockColor,
        val colorLeft: BlockColor,
        val colorRight: BlockColor,
        val colorFront: BlockColor,
        val colorBack: BlockColor,
        val type: Type
    ) {

        val noise = JNoise.newBuilder()
            .value(1, Interpolation.CUBIC, FadeFunction.NONE)
            .scale(512.0)
            .addModifier { v -> ((v + 1) / 2.0) * 0.35 + 0.65 }
            .clamp(0.0, 1.0)
            .build()

        fun create(position: Vector3i): Block {
            return Block(
                this,
                noise.evaluateNoise(position.x.toDouble(), position.y.toDouble(), position.z.toDouble()).toFloat()
            )
        }

    }

}