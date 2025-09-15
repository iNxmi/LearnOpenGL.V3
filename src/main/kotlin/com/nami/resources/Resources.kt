package com.nami.resources

import com.nami.resources.block.ResourceLoaderBlock
import com.nami.resources.feature.ResourceLoaderFeature
import com.nami.resources.item.ResourceLoaderItem
import com.nami.resources.language.ResourceLoaderLanguage
import com.nami.resources.model.ResourceLoaderModel
import com.nami.resources.particle.ResourceLoaderParticle
import com.nami.resources.recipe.ResourceLoaderRecipe
import com.nami.resources.shader.ResourceLoaderShader
import com.nami.resources.texture.ResourceLoaderTexture
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.isRegularFile

abstract class Resources<T : Resource>(
    val path: Path,
    private val prefix: String,
    private val extensions: Array<String>
) {

    companion object {
        val SHADER = ResourceLoaderShader()
        val TEXTURE = ResourceLoaderTexture()
        val BLOCK = ResourceLoaderBlock()
        val ITEM = ResourceLoaderItem()
        val MODEL = ResourceLoaderModel()
        val PARTICLE = ResourceLoaderParticle()
        val RECIPE = ResourceLoaderRecipe()
        val FEATURE = ResourceLoaderFeature()
        val LANGUAGE = ResourceLoaderLanguage()

        fun load(
            resources: Set<Resources<*>> = mutableSetOf(
                SHADER,
                TEXTURE,
                ITEM,
                BLOCK,
                FEATURE,
                MODEL,
                PARTICLE,
                RECIPE,
                LANGUAGE
            )
        ): Int {
            return resources.sumOf { it.load() }
        }

    }

    private val log = KotlinLogging.logger { }

    val map = mutableMapOf<String, T>()

    protected abstract fun onLoad(id: String, path: Path): T
    protected abstract fun onLoadCompleted()

    fun load(): Int {
        val paths = Files.walk(path)
            .filter { p -> p.isRegularFile() && extensions.contains(p.extension) }

        var errorCount = 0
        paths.forEach { p ->
            val id =
                p.subpath(path.nameCount, p.nameCount).invariantSeparatorsPathString.replace(
                    ".${p.extension}",
                    ""
                ).replace("/", ".")
            log.info { "Loading '$p' as '$id'" }

            try {
                val resource = onLoad(id, p)
                map[id] = resource
            } catch (e: Exception) {
                errorCount++
                log.warn { "Failed to load '$p'\n${e.stackTraceToString()}" }
            }
        }

        onLoadCompleted()

        return errorCount
    }

    fun get(key: String): T {
        val value = map[key] ?: throw NoSuchElementException("Resource '$prefix.$key' does not exist")

        return value
    }

}