package com.nami.world

import com.nami.Time
import com.nami.resources.GamePath
import com.nami.storage.Storage
import com.nami.world.chunk.ChunkManager
import com.nami.world.resources.block.BlockManagerSlow
import com.nami.world.resources.particle.ParticleManager
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.opengl.GL33.glClearColor
import java.nio.file.Path


class World(
    val name: String,
    val size: Vector3i,
    val seed: Long,
    val waterLevel: Int
) {

    val time = Time()

    val root: Path = GamePath.worlds.resolve(name)
    val fileName = "world"

    @Transient
    val blockManager = BlockManagerSlow(this)

    @Transient
    val chunkManager = ChunkManager(this)

    @Transient
    val particleManager = ParticleManager(this)

    val player = Player()

    fun update() {
        time.update()

        val color = Vector3f(52f / 255f, 146f / 255f, 235f / 255f).mul(1f)
        glClearColor(color.x, color.y, color.z, 1.0f)

        player.update(this)
        chunkManager.update(player, 6)
        particleManager.update()
    }

    fun render() {
        chunkManager.render(player, 6)
        particleManager.render(player)
    }

    fun write() {
        Storage.write(this, root, fileName)
    }

}