package com.nami.client.world.resources.feature.handlers

import com.nami.client.random
import com.nami.client.resources.Resources
import com.nami.client.world.World
import com.nami.client.world.resources.block.Block
import com.nami.client.world.resources.feature.FeatureListener
import org.joml.Vector3i

class FeatureHandlerCactus : FeatureListener {

    override fun generate(world: World, position: Vector3i): Map<Vector3i, Block.Instance> {
        val blocks = mutableMapOf<Vector3i, Block.Instance>()

        val height = Int.random(2..4)
        for (y in position.y + 1 until position.y + 1 + height)
            blocks[Vector3i(position.x, y, position.z)] =
                Resources.BLOCK.get("cactus").create(world, Vector3i(position.x, y, position.z))

        return blocks
    }

}