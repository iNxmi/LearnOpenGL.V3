package com.nami.world.biome

import com.nami.world.material.Material
import com.nami.world.material.MaterialSand
import com.nami.world.material.MaterialStone
import org.joml.Vector3i
import kotlin.math.roundToInt

object BiomeBeach : Biome(id = "beach") {

    override val elevation = 64f..67f
    override val moisture = 0f..100f
    override val temperature = -25f..50f

    override fun generateBlock(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Material? {
        val height = elevation.roundToInt()
        return when (position.y) {
            in (0 until height - 3) -> MaterialStone
            in (height - 3 until height) -> MaterialSand
            else -> null
        }
    }

}