package com.nami.core

import com.nami.core.Zip.Companion.compress
import com.nami.core.Zip.Companion.decompress
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

class Storage {

    companion object {

        inline fun <reified T> write(value: T, path: Path, name: String) {
            path.createDirectories()

            val jsonString = Json { prettyPrint = true }.encodeToString(value)
            val compressedString = compress(jsonString)

            val primary = path.resolve("${name}_primary.json")
            Files.write(primary, compressedString)

            val secondary = path.resolve("${name}_secondary.json")
            Files.write(secondary, compressedString)
        }

        inline fun <reified T> read(path: Path, name: String): T? {
            if (!path.exists())
                return null

            val primary = path.resolve("${name}_primary.json")
            val secondary = path.resolve("${name}_secondary.json")

            if (primary.exists()) {

                val compressedData = Files.readAllBytes(primary)
                val jsonString = decompress(compressedData)
                return Json.decodeFromString<T>(jsonString)

            } else if (secondary.exists()) {

                val compressedData = Files.readAllBytes(secondary)
                val jsonString = decompress(compressedData)
                return Json.decodeFromString<T>(jsonString)

            }

            return null
        }

    }

}