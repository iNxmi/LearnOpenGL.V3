package com.nami.world.chunk

import com.nami.world.biome.Biome
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.joml.Vector3i

@Serializable
data class Voxel(
    @Contextual val position: Vector3i,
    val biome: Biome.Instance,
    val block: Block?
)