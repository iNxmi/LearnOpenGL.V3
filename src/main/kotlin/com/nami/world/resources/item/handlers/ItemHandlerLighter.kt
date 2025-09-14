package com.nami.world.resources.item.handlers

import com.nami.random
import com.nami.world.World
import com.nami.world.entity.player.Player
import com.nami.world.resources.item.Item
import com.nami.world.resources.item.ItemListener
import org.joml.Vector3f
import org.joml.Vector3i

class ItemHandlerLighter : ItemListener {

    override fun onPrimaryUse(world: World, item: Item.Instance, player: Player): Boolean {
        val position = player.getFacingBlock(world)?.position ?: return false

        for (i in 0 until Int.random(16..32))
            world.particleManager.spawn(
                "fire", Vector3f(position).add(0.5f, 1f, 0.5f).add(
                    Vector3f(
                        ((Math.random() * 2 - 1) * 0.5).toFloat(),
                        ((Math.random() * 2 - 1) * 0.5).toFloat(), ((Math.random() * 2 - 1) * 0.5).toFloat()
                    ).normalize().mul(Math.random().toFloat())
                )
            )

        val block = world.blockManager.getBlock(position) ?: return false
        val radius = Int.random(3..7)
        if (block.template.id == "tnt")
            detonate(world, player, position, radius)

        return false
    }

    private fun detonate(world: World, player: Player, position: Vector3i, radius: Int) {
        for (z in -radius..radius)
            for (y in -radius..radius)
                for (x in -radius..radius)
                    if (x * x + y * y + z * z <= radius * radius) {
                        val pos = Vector3i(position).add(x, y, z)
                        val block = world.blockManager.getBlock(pos)

                        if (block != null) {
                            if (block.template.id == "tnt") {
                                world.blockManager.setBlock(Vector3i(pos), null)
                                detonate(world, player, Vector3i(pos), Int.random(3..7))
                            }

                            world.blockManager.setBlock(Vector3i(pos), null)
                        }

                        for (i in 0 until Int.random(0..5))
                            world.particleManager.spawn(
                                "fire",
                                Vector3f(pos).add(
                                    Vector3f(
                                        ((Math.random() * 2 - 1) * 0.5).toFloat(),
                                        ((Math.random() * 2 - 1) * 0.5).toFloat(),
                                        ((Math.random() * 2 - 1) * 0.5).toFloat()
                                    ).normalize().mul(Math.random().toFloat())
                                )
                            )

                        for (i in 0 until Int.random(12..20))
                            world.particleManager.spawn(
                                "explosion",
                                Vector3f(position).add(x.toFloat(), y.toFloat(), z.toFloat()).add(
                                    Vector3f(
                                        ((Math.random() * 2 - 1) * 0.5).toFloat(),
                                        ((Math.random() * 2 - 1) * 0.5).toFloat(),
                                        ((Math.random() * 2 - 1) * 0.5).toFloat()
                                    ).normalize().mul(Math.random().toFloat())
                                )
                            )

                    }
    }

    override fun onSecondaryUse(world: World, item: Item.Instance, player: Player): Boolean {
        return false
    }

}