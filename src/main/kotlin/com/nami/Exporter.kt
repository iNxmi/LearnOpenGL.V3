package com.nami

import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.nio.FloatBuffer
import java.nio.IntBuffer

class Exporter {

    companion object {
        @Throws(IOException::class)
        fun exportMesh(
            name: String?,
            positions: FloatArray,
            faces: IntArray,
            path: String?,
            dimension: Int
        ) {
            val sb = StringBuilder()

            sb.append(String.format("o %s\n", name))

            for (i in 0 until positions.size / 3) sb.append(
                String.format(
                    "v %s %s %s\n",
                    positions[i * dimension], positions[i * dimension + 1], positions[i * dimension + 2]
                )
            )

            for (i in 0 until faces.size / 3) {
                val i1 = faces[i * 3] + 1
                val i2 = faces[i * 3 + 1] + 1
                val i3 = faces[i * 3 + 2] + 1
                sb.append(String.format("f %s %s %s\n", i1, i2, i3))
            }

            val writer = BufferedWriter(FileWriter(path))
            writer.append(sb.toString())
            writer.close()
        }
    }

}