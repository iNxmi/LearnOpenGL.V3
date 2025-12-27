package com.nami.world.biome

import com.nami.world.block.Block
import com.nami.world.block.BlockDirt
import com.nami.world.block.BlockGrass
import com.nami.world.block.BlockStone
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

object BiomeJungleForest : Biome(id = "jungle_forest") {

    override val elevation = 67f..256f
    override val moisture = 75f..100f
    override val temperature = 20f..50f

    override fun generate(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? {
        val y = position.y
        val height = elevation.roundToInt()

        if ((height - 1 until height).contains(y))
            return BlockGrass

        if ((height - 4 until height - 1).contains(y))
            return BlockDirt

        if ((0 until height - 4).contains(y))
            return BlockStone

        return null
    }

}