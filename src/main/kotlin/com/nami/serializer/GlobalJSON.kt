package com.nami.serializer

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

object GlobalJSON {

    private val module = SerializersModule {
        contextual(SerializerClosedFloatingPointRangeFloat)
        contextual(SerializerIntRange)
        contextual(SerializerMatrix3f)
        contextual(SerializerMatrix4f)
        contextual(SerializerQuaternionf)
        contextual(SerializerVector2f)
        contextual(SerializerVector2i)
        contextual(SerializerVector3f)
        contextual(SerializerVector3i)
        contextual(SerializerVector4fHexadecimal)
        contextual(SerializerVector4iHexadecimal)
    }

    val instance = Json {
        serializersModule = module
        ignoreUnknownKeys = true
        prettyPrint = true
    }

}