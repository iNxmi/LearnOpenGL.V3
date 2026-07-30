package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import com.nami.world.block.blocks.BlockDirt
import com.nami.world.block.blocks.BlockGrass
import com.nami.world.block.blocks.BlockGravel
import com.nami.world.block.blocks.BlockStone
import com.nami.world.feature.features.FeatureOakTree
import de.articdive.jnoise.generators.noisegen.opensimplex.SuperSimplexNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise
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

object BiomeOakForest : Biome(id = "oak_forest") {

    override val elevation = 67f..256f
    override val moisture = 0f..100f
    override val temperature = 0f..35f

    override val features = setOf(

        (JNoise.newBuilder()
            .superSimplex(SuperSimplexNoiseGenerator.newBuilder().setSeed(0).build())
            .scale(1.0)
            .addModifier { v -> (v + 1) / 2.0 }
            .clamp(0.0, 1.0)
            .build() to FeatureOakTree)

    )

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
            if (temperature > 0f) {
                return BlockGrass
            } else {
                return BlockGravel
            }

        //Shallow
        if (density < 2.0f)
            if (temperature > 0f)
                return BlockDirt
            else
                return BlockGravel

        //Other
        return BlockStone
    }

}