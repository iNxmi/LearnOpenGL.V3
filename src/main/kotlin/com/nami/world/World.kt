package com.nami.world

import com.nami.entity.Transform
import com.nami.register.Register
import com.nami.scene.SceneTime
import de.articdive.jnoise.generators.noisegen.opensimplex.SuperSimplexNoiseGenerator
import de.articdive.jnoise.generators.noisegen.worley.WorleyNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise
import de.articdive.jnoise.transformers.domain_warp.DomainWarpTransformer
import mu.KotlinLogging
import org.joml.Math.sin
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.opengl.GL11.glClearColor
import java.lang.Float
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.math.*

class World {
    val log = KotlinLogging.logger { }

    val chunks = mutableMapOf<Pair<Int, Int>, Chunk>()
    val player = Player(this)

    val radiusChunkGen = 3
    val radiusChunkRender = 6

    private val noise = JNoise.newBuilder()
        .worley(WorleyNoiseGenerator.newBuilder().setSeed(System.currentTimeMillis()))
        .addDetailedTransformer(
            DomainWarpTransformer.newBuilder().setNoiseSource(SuperSimplexNoiseGenerator.newBuilder().build())
                .build()
        )
        .scale(0.004)
        .clamp(0.0, 1.0)
        .build()

    var t = 0f
    var tRemapped = 0f
    var sinT = 0f
    var cosT = 0f
    val secondsPerDay: Long = 24 * 60 * 60
    fun update(time: SceneTime) {
        t = (time.time % 60f) / 60f
//        t = (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) % secondsPerDay) / secondsPerDay.toFloat()
        tRemapped = (2f * PI * t - 0.5f * PI).toFloat()

        sinT = sin(tRemapped)
        cosT = cos(tRemapped)

        val color = Vector3f(0.45f, 0.84f, 1f).mul(max(sinT, 0f))
        glClearColor(color.x, color.y, color.z, 1.0f)

        val chunkPos = Vector2i(
            player.transform.position.x.toInt() / Chunk.width,
            player.transform.position.z.toInt() / Chunk.depth
        )

        for (x in -radiusChunkGen until radiusChunkGen)
            for (z in -radiusChunkGen until radiusChunkGen) {
                val cp = Vector2i(chunkPos).add(x, z)
                if (cp.x >= 0 && cp.y >= 0)
                    if (!chunks.containsKey(Pair(cp.x, cp.y)))
                        generateChunk(cp.x, cp.y)
            }

        player.update(time)

        chunks.forEach { (chunkPos, chunk) -> chunk.update() }
    }

    private fun generateChunk(chunkX: Int, chunkZ: Int) {
        val width = Chunk.width
        val height = Chunk.height
        val depth = Chunk.depth

        val chunk = Chunk()

        for (x in 0 until width)
            for (z in 0 until depth) {
                var height =
                    (noise.evaluateNoise(
                        (x + chunkX * Chunk.width).toDouble(),
                        (z + chunkZ * Chunk.depth).toDouble()
                    ) * height).roundToInt()
                height = height.coerceIn(1, Chunk.height)

                for (y in 0 until height) {
                    val color = if (y <= 0) {
                        Vector3f(0f, 0f, 1f).mul(0.75f)
                    } else if (y == 1) {
                        Vector3f(1f, 1f, 0f).mul(0.75f)
                    } else if (y > Chunk.height - Chunk.height / 3f) {
                        Vector3f(y.toFloat() / Chunk.height.toFloat())
                    } else {
                        Vector3f(0f, min((y.toFloat() / Chunk.height.toFloat()) + 0.3f, 1.0f), 0f)
                    }

                    color.mul((Math.random() * (1.0 - 0.91) + 0.91).toFloat())

                    chunk.blocks[Vector3i(x, if (y == 0) 1 else y, z)] = color
                }
            }

        chunks[Pair(chunkX, chunkZ)] = chunk
    }

    fun render() {
        val shader = Register.shader.get("chunk").bind()

        //TODO fix direction - its wrong
        val direction = Vector3f( -cosT, -sinT, 0f).normalize()
        shader.uniform.set("u_sin_t", sinT)
        shader.uniform.set("u_ambient_strength", min(max(sinT, 0.1f), 0.4f))
        shader.uniform.set("u_light_direction", direction)
        shader.uniform.set("u_specular_exponent", 16.0f)

        shader.uniform.set("u_projection_matrix", player.camera.projection)
        shader.uniform.set("u_view_matrix", player.camera.view)

        shader.uniform.set("u_camera_position", player.transform.position)

        for (x in -radiusChunkRender until radiusChunkRender)
            for (z in -radiusChunkRender until radiusChunkRender) {
                val pos = Pair(
                    player.transform.position.x.toInt() / Chunk.width + x,
                    player.transform.position.z.toInt() / Chunk.depth + z
                )
                val chunk = chunks[pos] ?: continue

                val transform = Transform()
                transform.position.set(
                    pos.first.toFloat() * Chunk.width,
                    0f,
                    pos.second.toFloat() * Chunk.depth
                )
                shader.uniform.set("u_model_matrix", transform.modelMatrix())

                chunk.render()
            }
//        chunks.forEach { (chunkPos, chunk) ->
//            val transform = Transform()
//            transform.position.set(
//                chunkPos.first.toFloat() * Chunk.width,
//                0f,
//                chunkPos.second.toFloat() * Chunk.depth
//            )
//            shader.uniform.set("u_model_matrix", transform.modelMatrix())
//
//            chunk.render()
//        }
    }

}