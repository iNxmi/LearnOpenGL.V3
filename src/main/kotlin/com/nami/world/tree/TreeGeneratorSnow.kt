package com.nami.world.tree

import com.nami.world.World
import org.joml.Vector3i
import kotlin.math.roundToInt

class TreeGeneratorSnow : TreeGenerator {
    override fun generate(position: Vector3i, world: World) {
        val blockManager = world.blockManager
        val noiseGenerators = world.noiseGenerators

        val baseHeight = (noiseGenerators.treeRandom.nextFloat() * 2 + 4).roundToInt()
        for (i in 0 until baseHeight)
            blockManager.setBlock(Vector3i(position).add(0, i + i, 0), "log", false)

        for (i in 0 until 5) {
            val position = Vector3i(position).add(0, baseHeight, 0)
            for (j in 5 until 15) {
                val rand = noiseGenerators.treeRandom.nextFloat()

                if ((0f..0.6f).contains(rand))
                    position.y += 1

                if ((0.6f..0.7f).contains(rand))
                    position.x += 1
                if ((0.7f..0.8f).contains(rand))
                    position.x -= 1

                if ((0.8f..0.9f).contains(rand))
                    position.z += 1
                if ((0.9f..1f).contains(rand))
                    position.z -= 1

                blockManager.setBlock(Vector3i(position), "log", false)

                if (blockManager.getBlock(Vector3i(position).add(1, 0, 0)) == null)
                    blockManager.setBlock(Vector3i(position).add(1, 0, 0), "leaves", false)
                if (blockManager.getBlock(Vector3i(position).add(-1, 0, 0)) == null)
                    blockManager.setBlock(Vector3i(position).add(-1, 0, 0), "leaves", false)
                if (blockManager.getBlock(Vector3i(position).add(0, 1, 0)) == null)
                    blockManager.setBlock(Vector3i(position).add(0, 1, 0), "leaves", false)
                if (blockManager.getBlock(Vector3i(position).add(0, -1, 0)) == null)
                    blockManager.setBlock(Vector3i(position).add(0, -1, 0), "leaves", false)
                if (blockManager.getBlock(Vector3i(position).add(0, 0, 1)) == null)
                    blockManager.setBlock(Vector3i(position).add(0, 0, 1), "leaves", false)
                if (blockManager.getBlock(Vector3i(position).add(0, 0, -1)) == null)
                    blockManager.setBlock(Vector3i(position).add(0, 0, -1), "leaves", false)
            }
        }
    }

}