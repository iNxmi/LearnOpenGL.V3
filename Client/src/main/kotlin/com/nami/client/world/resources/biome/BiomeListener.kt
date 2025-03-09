package com.nami.client.world.resources.biome

import com.nami.client.world.World
import com.nami.client.world.resources.block.Block
import org.joml.Vector3f
import org.joml.Vector3i

interface BiomeListener {

    fun onGenerateBlock(world: World, position: Vector3i, factors: Vector3f): Block?

}