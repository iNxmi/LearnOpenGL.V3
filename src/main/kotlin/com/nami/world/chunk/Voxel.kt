package com.nami.world.chunk

import com.nami.world.biome.Biome
import com.nami.world.block.Block
import org.joml.Vector3i

data class Voxel(
    val position: Vector3i,
    val biome: Biome.Instance,
    val block: Block?
)