package com.nami.client.world.resources.feature.handlers

import com.nami.client.world.World
import com.nami.client.world.resources.block.Block
import com.nami.client.world.resources.feature.FeatureListener
import org.joml.Vector3i

open class DefaultFeatureHandler : FeatureListener {

    override fun generate(world: World, position: Vector3i): Map<Vector3i, Block.Instance> {
        return mapOf()
    }

}