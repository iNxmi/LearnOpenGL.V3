package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockDirt
import com.nami.world.block.blocks.BlockGrass
import com.nami.world.block.blocks.BlockStone
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

    override fun generate(
        position: Vector3i,
        density: Float,
        densityAbove: Float,
        moisture: Float,
        temperature: Float
    ): Block? {
        //Air
        if (density <= 0)
            return null

        //Surface
        if (densityAbove <= 0)
            return BlockGrass

        //Shallow
        if (density <= 4.0f)
            return BlockDirt

        //Other
        return BlockStone
    }

}