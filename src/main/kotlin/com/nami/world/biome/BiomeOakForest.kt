package com.nami.world.biome

import com.nami.world.block.*
import org.joml.Vector3i
import kotlin.math.roundToInt

/*
  "features": [
    {
      "feature": "oak_tree",
      "base": [
        "grass",
        "dirt"
      ],
      "scale": 1024.0,
      "radius": 5
    }
  ]
}
 */

object BiomeOakForest : Biome(id="oak_forest") {

    override val elevation = 67f..256f
    override val moisture = 0f..100f
    override val temperature = 0f..35f

    override fun generate(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? {
        val y = position.y

        val height = elevation.roundToInt()
        if ((0 until height - 4).contains(y))
            return BlockStone

        if (temperature > 0) {
            if ((height - 4 until height - 1).contains(y))
                return BlockDirt

            if ((height - 1 until height).contains(y))
                return BlockGrass
        } else {
            if ((height - 4 until height).contains(y))
                return BlockGravel
        }

        return null
    }

}