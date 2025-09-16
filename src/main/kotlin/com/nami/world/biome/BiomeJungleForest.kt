package com.nami.world.biome

import com.nami.resources.Resources
import com.nami.world.resources.block.Block
import org.joml.Vector3i
import kotlin.math.roundToInt

/*
{
  "features": [
    {
      "feature": "jungle_tree",
      "base": [
        "grass",
        "dirt"
      ],
      "scale": 1024.0,
      "radius": 3
    }
  ]
}
 */

object BiomeJungleForest : Biome(id="jungle_forest") {

    override val elevation = 67f..256f
    override val moisture = 75f..100f
    override val temperature = 20f..50f

    override fun generateBlock(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? {
        val y = position.y

        val height = elevation.roundToInt()
        if ((0 until height - 4).contains(y))
            return Resources.BLOCK.get("stone")

        if ((height - 4 until height - 1).contains(y))
            return Resources.BLOCK.get("dirt")

        if ((height - 1 until height).contains(y))
            return Resources.BLOCK.get("grass")

        return null
    }

}