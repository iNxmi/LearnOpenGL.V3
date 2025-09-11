package com.nami.world

import com.nami.Time
import com.nami.json.JSONVector3i
import com.nami.resources.GamePath
import com.nami.storage.Storage
import com.nami.world.chunk.ChunkManager
import com.nami.world.entity.player.Player
import com.nami.world.resources.biome.BiomeManager
import com.nami.world.resources.block.BlockManager
import com.nami.world.resources.particle.ParticleManager
import kotlinx.serialization.Serializable
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.opengl.GL33.glClearColor
import java.io.IOException
import java.nio.file.Path

class World private constructor(
    val name: String,
    val size: Vector3i,
    val seed: Long,
    val waterLevel: Int
) {

    val time = Time()

    companion object {
        fun create(name: String, size: Vector3i, seed: Long = System.currentTimeMillis(), waterLevel: Int = 64) =
            World(name, size, seed, waterLevel)

        fun load(name: String): World {
            val root = GamePath.worlds.resolve(name)
            val fileName = "world"

            val json = Storage.read<JSON>(root, fileName)
                ?: throw IOException("World file could not be found")
            return json.create()
        }
    }

    val root: Path = GamePath.worlds.resolve(name)
    val fileName = "world"

    val biomeManager = BiomeManager(this)
    val blockManager = BlockManager(this)
    val chunkManager = ChunkManager(this)
    val particleManager = ParticleManager(this)

    val player = Player(this)

    init {
        val json = JSON(name, JSONVector3i(size), seed, waterLevel)
        Storage.write(json, root, fileName)
    }

    fun update() {
        time.update()

        val color = Vector3f(52f / 255f, 146f / 255f, 235f / 255f).mul(1f)
        glClearColor(color.x, color.y, color.z, 1.0f)

        player.update()
        chunkManager.update(player, 6)
        particleManager.update()
    }

    fun render() {
        chunkManager.render(player, 6)
        particleManager.render(player)
    }

    @Serializable
    private data class JSON(
        val name: String,
        val size: JSONVector3i,
        val seed: Long,
        val waterLevel: Int
    ) {
        fun create() = create(name, size.create(), seed, waterLevel)
    }

}