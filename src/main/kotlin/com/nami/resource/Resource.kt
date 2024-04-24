package com.nami.resource

import com.nami.resource.resoures.*
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.isRegularFile
import kotlin.io.path.nameWithoutExtension

abstract class Resource<T>(val path: Path, private val extension: String) {

    companion object {
        @JvmStatic
        val shader = ShaderResource()

        @JvmStatic
        val texture = TextureResource()

        @JvmStatic
        val block = BlockResource()
    }

    private val log = KotlinLogging.logger { }

    private val register = mutableMapOf<String, T>()

    protected abstract fun onLoad(path: Path): T

    fun load() {
        Files.list(path)
            .filter { p ->
                p.extension == extension && p.isRegularFile()
            }
            .forEach { p ->
                log.info { "Loading '$p'" }
                register[p.fileName.nameWithoutExtension] = onLoad(p)
            }
    }

    fun get(key: String): T {
        return register[key]!!
    }

}