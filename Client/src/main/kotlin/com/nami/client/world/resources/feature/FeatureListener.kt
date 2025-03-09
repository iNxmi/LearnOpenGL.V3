package com.nami.client.world.resources.feature

import com.nami.client.world.World
import com.nami.client.world.resources.block.Block
import org.joml.Vector3i

interface FeatureListener {

    fun generate(world: World, position: Vector3i): Map<Vector3i, Block.Instance>

}