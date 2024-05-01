package com.nami.world.biome

import org.joml.Vector3f

class BiomeRegister {

    companion object {

        private val map = mutableMapOf<Ranges, BiomeTemplate>()

        @JvmStatic
        fun register(biome: BiomeTemplate): BiomeTemplate {
            map[Ranges(biome.elevationRange, biome.moistureRange, biome.temperatureRange)] = biome
            return biome
        }

        fun evaluate(factors: Vector3f): BiomeTemplate {
            val e = factors.x
            val m = factors.y
            val t = factors.z

            map.forEach { (ranges, biome) ->
                if (ranges.elevation.contains(e) && ranges.moisture.contains(m) && ranges.temperature.contains(t))
                    return biome
            }

            return Biome.INVALID
        }

    }

    data class Ranges(
        val elevation: ClosedFloatingPointRange<Float>,
        val moisture: ClosedFloatingPointRange<Float>,
        val temperature: ClosedFloatingPointRange<Float>
    )

}