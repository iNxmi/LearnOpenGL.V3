package com.nami.world.tree

import com.nami.world.World
import com.nami.world.block.Block
import org.joml.Vector2i
import org.joml.Vector3i
import kotlin.math.roundToInt

class TreeGeneratorMushroom : TreeGenerator {
    override fun generate(position: Vector3i, world: World) {
        val blockManager = world.blockManager

        val x = position.x
        val z = position.z

        val height = blockManager.getHeight(Vector2i(x, z), 512, listOf(Block.Layer.SOLID))

        val stemHeight = (Math.random() * 4).roundToInt() + 7
        for (i in 0 until stemHeight) {
            val y = height + i + 1
            blockManager.setBlock(Vector3i(x, y, z), "mushroom_stem", false)

            if (i == stemHeight - 1) {
                blockManager.setBlock(Vector3i(x - 1, y + 1, z), "mushroom", false)
                blockManager.setBlock(Vector3i(x - 1, y + 1, z + 1), "mushroom", false)
                blockManager.setBlock(Vector3i(x - 1, y + 1, z + 2), "mushroom", false)

                blockManager.setBlock(Vector3i(x - 2, y + 1, z), "mushroom", false)

                blockManager.setBlock(Vector3i(x - 3, y + 1, z), "mushroom", false)
                blockManager.setBlock(Vector3i(x - 3, y + 1, z + 1), "mushroom", false)
                blockManager.setBlock(Vector3i(x - 3, y + 1, z + 2), "mushroom", false)

                blockManager.setBlock(Vector3i(x + 1, y + 1, z), "mushroom", false)
                blockManager.setBlock(Vector3i(x + 2, y + 1, z), "mushroom", false)
                blockManager.setBlock(Vector3i(x + 3, y + 1, z), "mushroom", false)

                blockManager.setBlock(Vector3i(x, y + 1, z - 1), "mushroom", false)
                blockManager.setBlock(Vector3i(x, y + 1, z - 2), "mushroom", false)
                blockManager.setBlock(Vector3i(x, y + 1, z - 3), "mushroom", false)

                blockManager.setBlock(Vector3i(x, y + 1, z + 1), "mushroom", false)
                blockManager.setBlock(Vector3i(x, y + 1, z + 2), "mushroom", false)
                blockManager.setBlock(Vector3i(x, y + 1, z + 3), "mushroom", false)
            }
        }
    }

}