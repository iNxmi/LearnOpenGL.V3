package com.nami.world.biome.biomes

import com.nami.world.biome.Biome
import com.nami.world.block.*
import com.nami.world.block.blocks.BlockDirt
import com.nami.world.block.blocks.BlockGrass
import com.nami.world.block.blocks.BlockGravel
import com.nami.world.block.blocks.BlockStone
import com.nami.world.feature.features.FeatureBirchTree
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

    override fun generate(position: Vector3i, elevation: Float, moisture: Float, temperature: Float): Block? {
        val y = position.y

        val height = elevation.roundToInt()
        if ((0 until height - 4).contains(y))
            return BlockStone

        if (temperature > 0) {
            if ((height - 4 until height - 1).contains(y))
                return BlockDirt

            if ((height - 1 until height).contains(y))
                return BlockGrass
        } else {
            if ((height - 4 until height).contains(y))
                return BlockGravel
        }

        return null
    }

}