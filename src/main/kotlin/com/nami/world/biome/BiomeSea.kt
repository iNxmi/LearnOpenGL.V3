package com.nami.world.biome

import com.nami.world.material.*
import org.joml.Vector3i
import kotlin.math.roundToInt

object BiomeSea : Biome(id = "sea") {

    override val elevation = 0f..64f
    override val moisture = 0f..100f
    override val temperature = -25f..50f

    override fun generateBlock(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Material? {
        val y = position.y

        val height = elevation.roundToInt()
        if ((0 until height - 3).contains(y))
            return MaterialStone

        if ((height - 3 until height).contains(y))
            return MaterialGravel

        if ((height until 64).contains(y))
            return MaterialWater

        return null
    }

}