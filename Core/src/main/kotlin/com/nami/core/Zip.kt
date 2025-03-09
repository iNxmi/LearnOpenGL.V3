package com.nami.core

import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.text.Charsets.UTF_8

class Zip {

    companion object {

        fun compress(str: String): ByteArray {
            val bos = ByteArrayOutputStream()
            GZIPOutputStream(bos).bufferedWriter(UTF_8).use { it.write(str) }

            return bos.toByteArray()
        }

        fun decompress(data: ByteArray): String {
            return GZIPInputStream(data.inputStream()).bufferedReader(UTF_8).use { it.readText() }
        }

    }

}