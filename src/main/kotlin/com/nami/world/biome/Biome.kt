package com.nami.world.biome

import com.nami.world.biome.generator.*
import com.nami.world.block.Block
import org.joml.Vector3f
import org.joml.Vector3i

class Biome(val template: BiomeTemplate, val factors: Vector3f) {

    companion object {
        @JvmStatic
        val ELEVATION_RANGE: ClosedFloatingPointRange<Float> = (0.0f..256.0f)

        @JvmStatic
        val MOISTURE_RANGE: ClosedFloatingPointRange<Float> = (0.0f..100.0f)

        @JvmStatic
        val TEMPERATURE_RANGE: ClosedFloatingPointRange<Float> = (-25.0f..50.0f)

        @JvmStatic
        val INVALID = BiomeTemplate(
            "Invalid",
            0f..0f,
            0f..0f,
            0f..0f,
            InvalidBiomeGenerator()
        )

        @JvmStatic
        val SEA = BiomeRegister.register(
            BiomeTemplate(
                "Sea",
                0f..64f,
                0f..100f,
                -15f..50f,
                SeaBiomeGenerator()
            )
        )

        @JvmStatic
        val BEACH = BiomeRegister.register(
            BiomeTemplate(
                "Beach",
                64f..67f,
                0f..100f,
                0f..50f,
                BeachBiomeGenerator()
            )
        )

        @JvmStatic
        val MUSHROOM = BiomeRegister.register(
            BiomeTemplate(
                "Mushroom",
                67f..256f,
                65f..100f,
                25f..35f,
                MushroomBiomeGenerator()
            )
        )

        @JvmStatic
        val NORMAL = BiomeRegister.register(
            BiomeTemplate(
                "Normal",
                67f..256f,
                0f..100f,
                0f..35f,
                NormalBiomeGenerator()
            )
        )

        @JvmStatic
        val SNOW = BiomeRegister.register(
            BiomeTemplate(
                "Snow",
                67f..256f,
                0f..100f,
                -25f..0f,
                SnowBiomeGenerator()
            )
        )

        @JvmStatic
        val DESERT = BiomeRegister.register(
            BiomeTemplate(
                "Desert",
                67f..256f,
                0f..50f,
                35f..50.0f,
                DesertBiomeGenerator()
            )
        )

        @JvmStatic
        fun evaluate(factors: Vector3f): Biome {
            val realFactors = Vector3f(
                factors.x * (ELEVATION_RANGE.endInclusive - ELEVATION_RANGE.start) + ELEVATION_RANGE.start,
                factors.y * (MOISTURE_RANGE.endInclusive - MOISTURE_RANGE.start) + MOISTURE_RANGE.start,
                factors.z * (TEMPERATURE_RANGE.endInclusive - TEMPERATURE_RANGE.start) + TEMPERATURE_RANGE.start
            )
            return Biome(BiomeRegister.evaluate(realFactors), realFactors)
        }

    }

    fun generate(position: Vector3i): Block.Template? {
        return template.generator.generate(factors, position)
    }

}

