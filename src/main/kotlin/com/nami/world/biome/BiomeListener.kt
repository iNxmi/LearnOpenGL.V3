package com.nami.world.biome

import org.joml.Vector3f
import org.joml.Vector3i

interface BiomeListener {

    fun onGenerateBlock(position: Vector3i, factors: Vector3f): String?

}