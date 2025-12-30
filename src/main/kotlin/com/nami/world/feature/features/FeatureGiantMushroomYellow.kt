package com.nami.world.feature.features

import com.nami.world.block.blocks.BlockMushroomYellow
import com.nami.world.feature.FeatureGiantMushroom

object FeatureGiantMushroomYellow : FeatureGiantMushroom(
    BlockMushroomYellow,
    4..7,
    2..4,
    id = "mushroom_yellow"
)