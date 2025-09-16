package com.nami.world.biome

import com.nami.resources.Resources
import com.nami.world.resources.block.Block
import org.joml.Vector3i
import kotlin.math.roundToInt

/*
{
  "features": [
    {
      "feature": "birch_tree",
      "base": [
        "podzol",
        "dirt"
      ],
      "scale": 1024.0,
      "radius": 4
    }
  ]
}
 */

object BiomeBirchForest : Biome(id = "birch_forest") {

    override val elevation = 67f..256f
    override val moisture = 50f..100f
    override val temperature = 5f..25f

    override fun generateBlock(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? {
        val y = position.y

        val height = elevation.roundToInt()
        if ((0 until height - 4).contains(y))
            return Resources.BLOCK.get("stone")

        if (temperature > 0) {
            if ((height - 4 until height - 1).contains(y))
                return Resources.BLOCK.get("dirt")

            if ((height - 1 until height).contains(y))
                return Resources.BLOCK.get("podzol")
        } else {
            if ((height - 4 until height).contains(y))
                return Resources.BLOCK.get("gravel")
        }

        return null
    }

}