package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockSand
import com.nami.world.block.blocks.BlockStone
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

    override val density = 67f..256f
    override val moisture = 0f..20f
    override val temperature = 25f..50f

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
        if (densityAbove <= 0f)
            return BlockSand

        //Shallow
        if (density <= 4.0f)
            return BlockSand

        //Other
        return BlockStone
    }

}