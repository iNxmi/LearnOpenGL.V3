package com.nami.world.biome

import com.nami.world.biome.rules.*
import com.nami.world.block.BlockTemplate
import org.joml.Vector2i
import org.joml.Vector3f

class Biome(val factors: Vector3f, val generator: BiomeGenerator) {

    companion object {
        const val waterLevel: Int = 64

        val elevationRange: ClosedFloatingPointRange<Float> = (0.0f..256.0f)            //Meter
        val moistureRange: ClosedFloatingPointRange<Float> = (0.0f..100.0f)          //Percent
        val temperatureRange: ClosedFloatingPointRange<Float> = (-25.0f..50.0f)      //Celsius

        val sea = SeaBiomeGenerator()
        val beach = BeachBiomeGenerator()
        val normal = NormalBiomeGenerator()
        val snow = SnowBiomeGenerator()
        val desert = DesertBiomeGenerator()

        fun evaluate(factors: Vector3f): Biome {
            return Biome(factors, evaluateGenerator(factors))
        }

        private fun evaluateGenerator(factors: Vector3f): BiomeGenerator {
            val e = factors.x
            val m = factors.y
            val t = factors.z

            if (e < 64.0f) return sea
            if (e < 67f) return beach

            if(t > 35.0f) return desert
            if(t < -10.0f) return snow

            if (e >= 192f) return snow
            if (e >= 67f) return normal

            return normal
        }
    }

    fun generateColumn(position: Vector2i): Map<Int, BlockTemplate> {
        return generator.generateColumn(factors, position)
    }

    override fun toString(): String {
        return "Biome=${generator.name()} Elevation=${factors.x} Moisture=${factors.y} Temperature=${factors.z}"
    }

}

