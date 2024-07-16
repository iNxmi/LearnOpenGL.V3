package com.nami.world.biome

import com.nami.world.World
import org.joml.Vector3f
import org.joml.Vector3i

interface BiomeListener {

    fun onGenerateBlock(world: World, position: Vector3i, factors: Vector3f): String?

}