package com.nami.world.feature.features

import com.nami.world.block.blocks.BlockMushroomRed
import com.nami.world.feature.FeatureGiantMushroom

object FeatureGiantMushroomRed : FeatureGiantMushroom(
    BlockMushroomRed,
    7..11,
    2..4,
    id = "mushroom_red"
)