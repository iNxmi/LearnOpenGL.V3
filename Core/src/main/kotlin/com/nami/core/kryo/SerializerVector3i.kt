package com.nami.core.kryo

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.Serializer
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.joml.Vector3i

class SerializerVector3i : Serializer<Vector3i>() {

    override fun write(kryo: Kryo, output: Output, vector: Vector3i) {
        output.writeInt(vector.x)
        output.writeInt(vector.y)
        output.writeInt(vector.z)
    }

    override fun read(kryo: Kryo, input: Input, type: Class<Vector3i>): Vector3i {
        return Vector3i(
            input.readInt(),
            input.readInt(),
            input.readInt()
        )
    }

}