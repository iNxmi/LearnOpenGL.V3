package com.nami.world

import com.nami.resources.GamePath
import com.nami.scene.SceneTime
import com.nami.world.biome.BiomeManager
import com.nami.world.block.BlockManager
import com.nami.world.chunk.ChunkManager
import com.nami.world.particle.ParticleManager
import com.nami.world.player.Player
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.opengl.GL33.glClearColor
import kotlin.io.path.createDirectories

class World(
    val seed: Long,
    val name: String,
    val time: SceneTime,

    val size: Vector3i,
    val waterLevel: Int
) {

    val path = GamePath.worlds.resolve(name)

    val biomeManager = BiomeManager(this)
    val blockManager = BlockManager(this)
    val chunkManager = ChunkManager(this)
    val particleManager = ParticleManager(this)

    val player = Player(this)

    fun update(time: SceneTime) {
        val color = Vector3f(52f / 255f, 146f / 255f, 235f / 255f).mul(1f)
        glClearColor(color.x, color.y, color.z, 1.0f)

        player.update(time)
        blockManager.update(time)
        chunkManager.update(time, player, 6)
        particleManager.update(time)
    }

    fun render(time: SceneTime) {
        chunkManager.render(time, player, 6)
        particleManager.render(player, time)
    }

}