package com.nami.world.inventory.item.handlers

import com.nami.world.inventory.item.ItemListener
import com.nami.world.player.Player
import org.joml.Vector3i
import kotlin.math.roundToInt

class ItemHandlerIDacorn : ItemListener {
    override fun onPrimaryUse(player: Player): Boolean {
        val blockManager = player.world.blockManager
        val noiseGenerators = player.world.noiseGenerators
        val block = player.getFacingBlock() ?: return false
        val position = block.position

        val baseHeight = (noiseGenerators.treeRandom.nextFloat() * 2 + 4).roundToInt()
        for (i in 0 until baseHeight)
            blockManager.setBlock(Vector3i(position.x, position.y + 1 + i, position.z), "log", false)

        for (i in 0 until 5) {
            val position = Vector3i(position.x, position.y + baseHeight, position.z)
            for (j in 5 until 15) {
                val rand = noiseGenerators.treeRandom.nextFloat()

                if ((0f..0.4f).contains(rand))
                    position.y += 1

                if ((0.4f..0.55f).contains(rand))
                    position.x += 1
                if ((0.55f..0.7f).contains(rand))
                    position.x -= 1

                if ((0.7f..0.85f).contains(rand))
                    position.z += 1
                if ((0.85f..1f).contains(rand))
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

        player.world.chunkManager.getByBlockPosition(position)?.updateMesh()

        return true
    }

    override fun onSecondaryUse(player: Player): Boolean {
        return false
    }

}