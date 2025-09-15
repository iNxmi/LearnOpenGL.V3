package com.nami.world.biome

import com.nami.resources.Resources
import com.nami.world.resources.block.Block
import org.joml.Vector3i
import kotlin.math.roundToInt

object BiomeBeach : Biome() {

    override val elevation = 64f..67f
    override val moisture = 0f..100f
    override val temperature = -25f..50f

    override fun generate(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? {
        val height = elevation.roundToInt()
        return when (position.y) {
            in (0 until height - 3) -> Resources.BLOCK.get("stone")
            in (height - 3 until height) -> Resources.BLOCK.get("sand")
            else -> null
        }
    }

}