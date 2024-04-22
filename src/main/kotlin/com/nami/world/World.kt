package com.nami.world

import com.nami.resource.Resource
import com.nami.scene.SceneTime
import de.articdive.jnoise.core.api.functions.Interpolation
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction
import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator
import de.articdive.jnoise.generators.noisegen.worley.WorleyNoiseGenerator
import de.articdive.jnoise.pipeline.JNoise
import mu.KotlinLogging
import org.joml.Math.sin
import org.joml.Vector2i
import org.joml.Vector3f
import org.joml.Vector3i
import org.lwjgl.opengl.GL33.glClearColor
import java.time.LocalTime
import kotlin.math.*

class World {
    val log = KotlinLogging.logger { }

    val chunks = mutableMapOf<Vector2i, Chunk>()
    val player = Player(this)

    val chunkRadius = 5

    private val noise1 = JNoise.newBuilder()
        .perlin(System.currentTimeMillis(), Interpolation.LINEAR, FadeFunction.IMPROVED_PERLIN_NOISE)
        .scale(1 / 50.0)
        .clamp(0.0, 1.0)
        .build()

    private val noise2 = JNoise.newBuilder()
        .fastSimplex(FastSimplexNoiseGenerator.newBuilder().setSeed(System.currentTimeMillis() + 1).build())
        .scale(1 / 89.0)
        .clamp(-1.0, 1.0)
        .build()

    var worldTime = 0f
    var daylightPercentage = 0f

    var sinT = 0f
    var cosT = 0f

    val millisPerDay: Long = 24 * 60 * 60 * 1000

    fun update(time: SceneTime) {
        val localTime = LocalTime.now()
        val millisOfDay =
            (localTime.hour * 3600000 + localTime.minute * 60000 + localTime.second * 1000 + localTime.nano / 1000000).toLong()
        worldTime = (millisOfDay % millisPerDay) / millisPerDay.toFloat()

//        val dayLength = 60f
//        worldTime = (time.time % dayLength) / dayLength

        worldTime = 0.35f

        val functionResult = daylightFunction(worldTime)
        daylightPercentage = functionResult.first

        val t: Float = (2.0 * PI * functionResult.second - PI / 2.0).toFloat()
        sinT = sin(t)
        cosT = cos(t)

        val color = Vector3f(0.45f, 0.84f, 1f).mul(daylightPercentage)
        glClearColor(color.x, color.y, color.z, 1.0f)

        player.update(time)

        for (x in -chunkRadius until chunkRadius)
            for (z in -chunkRadius until chunkRadius) {
                val pos = Vector2i(
                    player.transform.position.x.toInt() / Chunk.width + x,
                    player.transform.position.z.toInt() / Chunk.depth + z
                )

                if (pos.x >= 0 && pos.y >= 0)
                    if (!chunks.containsKey(pos))
                        chunks[pos] = Chunk(this, pos.x, pos.y, noise1, noise2)

                val chunk = chunks[pos] ?: continue
                chunk.update()
            }
    }

    private fun daylightFunction(t: Float): Pair<Float, Float> {
        //00:00 Uhr - 06:00 Uhr
        var range = 0f / 24f..6f / 24F
        if (range.contains(t))
            return Pair(0f, 0f)

        //06:00 Uhr - 10:00 Uhr
        range = 6f / 24f..10f / 24f
        if (range.contains(t)) {
            val x = (t - range.start) / (range.endInclusive - range.start)
            val percentage = ((x - 1).pow(2f) - 1f).pow(2f)

            return Pair(percentage, percentage / 2f)
        }

        //10:00 Uhr - 17:00 Uhr
        range = 10f / 24f..17f / 24f
        if (range.contains(t))
            return Pair(1f, 0.5f)

        //17:00 Uhr - 21:00 Uhr
        range = 17f / 24f..21f / 24f
        if (range.contains(t)) {
            val x = (t - range.start) / (range.endInclusive - range.start)
            val percentage = (x.pow(2f) - 1f).pow(2f)

            return Pair(percentage, percentage / 2f - 0.5f / percentage)
        }

        //21:00 Uhr - 24:00 Uhr
        range = 21f / 24f..24f / 24f
        if (range.contains(t))
            return Pair(0f, 0f)

        //Fallback -> Redundant to (21:00 Uhr - 24:00 Uhr)
        return Pair(0f, 0f)
    }

    fun render() {
        val shader = Resource.shader.get("chunk").bind()

//        val direction = Vector3f(cosT, sinT, 0f)
        val direction = Vector3f(-1f, 1f, 0f).normalize()
        shader.uniform.set("u_daylight_percentage", daylightPercentage)
        shader.uniform.set("u_ambient_strength", min(max(daylightPercentage, 0.1f), 0.4f))
        shader.uniform.set("u_light_direction", direction)
        shader.uniform.set("u_specular_exponent", 8.0f)

        shader.uniform.set("u_projection_matrix", player.camera.projection)
        shader.uniform.set("u_view_matrix", player.camera.view)

        shader.uniform.set("u_camera_position", player.transform.position)

        for (x in -chunkRadius until chunkRadius)
            for (z in -chunkRadius until chunkRadius) {
                val pos = Vector2i(
                    player.transform.position.x.toInt() / Chunk.width + x,
                    player.transform.position.z.toInt() / Chunk.depth + z
                )

                val chunk = chunks[pos] ?: continue
                chunk.render(shader)
            }

        Resource.shader.unbind()
    }

    fun getBlock(position: Vector3i): Block? {
        val chunkPos = Vector2i(position.x / Chunk.width, position.z / Chunk.depth)
        val chunk = chunks[chunkPos] ?: return null

        val chunkRelativeBlockPos =
            Vector3i(position.x - chunkPos.x * Chunk.width, position.y, position.z - chunkPos.y * Chunk.depth)
        return chunk.blocks[chunkRelativeBlockPos]
    }

    fun setBlock(position: Vector3i, block: Block?) {
        if (position.y >= Chunk.height)
            return

        val chunkPos = Vector2i(position.x / Chunk.width, position.z / Chunk.depth)

        if (!chunks.containsKey(chunkPos))
            chunks[chunkPos] = Chunk(this, chunkPos.x, chunkPos.y, noise1, noise2)

        val chunk = chunks[chunkPos]!!

        val chunkRelativeBlockPos =
            Vector3i(position.x - chunkPos.x * Chunk.width, position.y, position.z - chunkPos.y * Chunk.depth)

        if (block != null)
            chunk.blocks[chunkRelativeBlockPos] = block
        else
            chunk.blocks.remove(chunkRelativeBlockPos)
    }

    fun getHeight(pos: Vector2i): Int {
        for (y in Chunk.height downTo 0)
            if (getBlock(Vector3i(pos.x, y, pos.y)) != null)
                return y

        return 0
    }

}