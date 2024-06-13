package com.nami.resources

import com.nami.resources.biome.ResourceBiome
import com.nami.resources.block.ResourceBlock
import com.nami.resources.item.ResourceItem
import com.nami.resources.shader.ResourceShader
import com.nami.resources.texture.ResourceTexture
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.invariantSeparatorsPathString
import kotlin.io.path.isRegularFile

abstract class Resource<T>(val path: Path, private val extensions: Array<String>) {

    companion object {
        @JvmStatic
        val SHADER = ResourceShader()

        @JvmStatic
        val TEXTURE = ResourceTexture()

        @JvmStatic
        val BLOCK = ResourceBlock()

        @JvmStatic
        val BIOME = ResourceBiome()

        @JvmStatic
        val ITEM = ResourceItem()
    }

    private val log = KotlinLogging.logger { }

    val map = mutableMapOf<String, T>()

    protected abstract fun onLoad(path: Path): T
    protected abstract fun onLoadCompleted()

    fun load() {
        Files.walk(path)
            .filter { p ->
                p.isRegularFile() && extensions.contains(p.extension)
            }
            .forEach { p ->
                val key =
                    p.subpath(path.nameCount, p.nameCount).invariantSeparatorsPathString.replace(".${p.extension}", "")
                        .replace("/", ".")
                log.info { "Loading '$p' as '$key'" }

                map[key] = onLoad(p)
            }

        onLoadCompleted()
    }

    fun add(key: String, value: T): Boolean {
        if (map.containsKey(key))
            return false

        map[key] = value
        return true
    }

    fun get(key: String): T {
        return map[key]!!
    }

}