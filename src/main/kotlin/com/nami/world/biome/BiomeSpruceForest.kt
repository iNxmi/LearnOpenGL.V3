package com.nami.world.biome

import com.nami.world.material.Material
import com.nami.world.material.MaterialIce
import com.nami.world.material.MaterialSnow
import com.nami.world.material.MaterialStone
import org.joml.Vector3i
import kotlin.math.roundToInt

object BiomeSpruceForest : Biome(id = "spruce_forest") {

    override val elevation = 67f..256f
    override val moisture = 0f..100f
    override val temperature = -25f..0f

    override fun generateBlock(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Material? {
        val y = position.y

        val height = elevation.roundToInt()
        if ((0 until height - 4).contains(y))
            return MaterialStone

        if ((height - 4 until height).contains(y))
            return if (temperature < -5) MaterialIce else MaterialSnow

        return null
    }

}