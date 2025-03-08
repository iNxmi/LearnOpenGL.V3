package com.nami.world.resources.feature

import com.nami.world.World
import com.nami.world.resources.block.Block
import org.joml.Vector3i

interface FeatureListener {

    fun generate(world: World, position: Vector3i): Map<Vector3i, Block.Instance>

}