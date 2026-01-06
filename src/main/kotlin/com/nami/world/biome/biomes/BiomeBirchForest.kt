package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockDirt
import com.nami.world.block.blocks.BlockGravel
import com.nami.world.block.blocks.BlockPodzol
import com.nami.world.block.blocks.BlockStone
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

    override fun generate(
        position: Vector3i,
        density: Float,
        densityAbove: Float,
        moisture: Float,
        temperature: Float
    ): Block? {

        //Air
        if (density <= 0f)
            return null

        //Surface
        if (densityAbove <= 0f) {
            return if (temperature > 0f)
                BlockPodzol
            else
                BlockGravel
        }

        //Shallow
        if (density < 4.0f) {
            return if (temperature > 0f)
                BlockDirt
            else
                BlockGravel
        }

        //Other
        return BlockStone
    }

}