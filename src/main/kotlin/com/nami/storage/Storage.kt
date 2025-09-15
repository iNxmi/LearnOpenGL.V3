package com.nami.storage

import com.nami.serializer.GlobalJSON
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.text.Charsets.UTF_8

class Storage {

    companion object {

        val log = KotlinLogging.logger {}
        val raw = true

        inline fun <reified T> write(value: T, path: Path, name: String) {
            path.createDirectories()

            val jsonString = GlobalJSON.instance.encodeToString(value)
            val compressedString = compress(jsonString)

            val primary = path.resolve("${name}_primary.json")
            Files.write(primary, compressedString)

            val secondary = path.resolve("${name}_secondary.json")
            Files.write(secondary, compressedString)

            if(raw) {
                val raw = path.resolve("${name}_raw.json")
                Files.writeString(raw, jsonString)
            }
        }

        inline fun <reified T> read(path: Path, name: String): T? {
            if (!path.exists())
                return null

            val primary = path.resolve("${name}_primary.json")
            val secondary = path.resolve("${name}_secondary.json")

            if (primary.exists()) {
                val compressedData = Files.readAllBytes(primary)
                val jsonString = decompress(compressedData)
                return GlobalJSON.instance.decodeFromString<T>(jsonString)
            } else if (secondary.exists()) {
                val compressedData = Files.readAllBytes(secondary)
                val jsonString = decompress(compressedData)
                return GlobalJSON.instance.decodeFromString<T>(jsonString)
            }

            return null
        }

        fun compress(str: String): ByteArray {
            val bos = ByteArrayOutputStream()
            GZIPOutputStream(bos).bufferedWriter(UTF_8).use { it.write(str) }

            return bos.toByteArray()
        }

        fun decompress(data: ByteArray) =
            GZIPInputStream(data.inputStream()).bufferedReader(UTF_8).use { it.readText() }

    }

}