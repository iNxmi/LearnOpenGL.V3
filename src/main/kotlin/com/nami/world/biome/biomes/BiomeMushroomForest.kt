package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockDirt
import com.nami.world.block.blocks.BlockMycelium
import com.nami.world.block.blocks.BlockStone
import org.joml.Vector3i
import kotlin.math.roundToInt

/*
{
  "features": [
    {
      "feature": "giant_mushroom_red",
      "base": [
        "mycelium",
        "dirt"
      ],
      "scale": 1024.0,
      "radius": 5
    },
    {
      "feature": "giant_mushroom_yellow",
      "base": [
        "mycelium",
        "dirt"
      ],
      "scale": 1024.0,
      "radius": 30
    }
  ]
}
 */

object BiomeMushroomForest : Biome(id = "mushroom_forest") {

    override val elevation = 67f..256f
    override val moisture = 50f..100f
    override val temperature = 20f..40f

    override fun generate(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? {
        val y = position.y

        val height = elevation.roundToInt()
        if ((0 until height - 4).contains(y))
            return BlockStone

        if ((height - 4 until height - 1).contains(y))
            return BlockDirt

        if ((height - 1 until height).contains(y))
            return BlockMycelium

        return null
    }

}