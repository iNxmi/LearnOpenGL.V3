package com.nami.resources

import com.nami.resources.biome.ResourceLoaderBiome
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

abstract class Resources<T: Resource>(val path: Path, private val prefix: String, private val extensions: Array<String>) {

    companion object {
        @JvmStatic
        val SHADER = ResourceLoaderShader()

        @JvmStatic
        val TEXTURE = ResourceLoaderTexture()

        @JvmStatic
        val BLOCK = ResourceLoaderBlock()

        @JvmStatic
        val BIOME = ResourceLoaderBiome()

        @JvmStatic
        val ITEM = ResourceLoaderItem()

        @JvmStatic
        val MODEL = ResourceLoaderModel()

        @JvmStatic
        val PARTICLE = ResourceLoaderParticle()

        @JvmStatic
        val RECIPE = ResourceLoaderRecipe()

        @JvmStatic
        val FEATURE = ResourceLoaderFeature()

        @JvmStatic
        val LANGUAGE = ResourceLoaderLanguage()
    }

    private val log = KotlinLogging.logger { }

    val map = mutableMapOf<String, T>()

    protected abstract fun onLoad(id: String, path: Path): T
    protected abstract fun onLoadCompleted()

    fun load(): Int {
        var errorCount = 0
        Files.walk(path)
            .filter { p ->
                p.isRegularFile() && extensions.contains(p.extension)
            }
            .forEach { p ->
                val id =
                    p.subpath(path.nameCount, p.nameCount).invariantSeparatorsPathString.replace(
                        ".${p.extension}",
                        ""
                    ).replace("/", ".")
                log.info { "Loading '$p' as '$id'" }

                try {
                    map[id] = onLoad(id, p)
                } catch (e: Exception) {
                    errorCount++
                    log.warn { "Failed to load '$p'\n${e.message}" }
                }
            }

        onLoadCompleted()

        return errorCount
    }

    fun add(key: String, value: T): Boolean {
        if (map.containsKey(key))
            return false

        map[key] = value
        return true
    }

    fun get(key: String): T {
        val value = map[key] ?: throw NoSuchElementException("Resource '$prefix.$key' does not exist")

        return value
    }

}