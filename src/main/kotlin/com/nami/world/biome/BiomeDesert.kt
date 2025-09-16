package com.nami.world.biome

import com.nami.resources.Resources
import com.nami.world.resources.block.Block
import org.joml.Vector3i
import kotlin.math.roundToInt

/*
{
  "features": [
    {
      "feature": "cactus",
      "base": [
        "sand"
      ],
      "scale": 1024.0,
      "radius": 10
    }
  ]
}
 */

object BiomeDesert : Biome(id = "desert") {

    override val elevation = 67f..256f
    override val moisture = 0f..20f
    override val temperature = 25f..50f

    override fun generateBlock(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? {
        val y = position.y

        val height = elevation.roundToInt()
        if ((0 until height - 3).contains(y))
            return Resources.BLOCK.get("stone")

        if ((height - 3 until height).contains(y))
            return Resources.BLOCK.get("sand")

        return null
    }

}