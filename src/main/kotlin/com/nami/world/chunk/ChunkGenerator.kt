package com.nami.world.chunk

import de.articdive.jnoise.pipeline.JNoise

class ChunkGenerator(
    val elevationNoise: JNoise,
    val moistureNoise: JNoise,
    val temperatureNoise: JNoise,
    val caveNoise: JNoise,
    val treeNoise: JNoise
)