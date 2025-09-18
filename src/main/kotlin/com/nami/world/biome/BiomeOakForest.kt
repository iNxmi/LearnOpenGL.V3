package com.nami.world.biome

import com.nami.world.material.*
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

    override fun generateBlock(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Material? {
        val y = position.y

        val height = elevation.roundToInt()
        if ((0 until height - 4).contains(y))
            return MaterialStone

        if (temperature > 0) {
            if ((height - 4 until height - 1).contains(y))
                return MaterialDirt

            if ((height - 1 until height).contains(y))
                return MaterialGrass
        } else {
            if ((height - 4 until height).contains(y))
                return MaterialGravel
        }

        return null
    }

}