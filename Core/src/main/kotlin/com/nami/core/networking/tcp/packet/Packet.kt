package com.nami.core.networking.tcp.packet

import kotlinx.serialization.json.*

class Packet(
    val type: Type,
    val data: JsonObject = buildJsonObject {}
) {

    constructor(json: JsonObject) : this(
        Type.values()[json.getOrDefault("type", JsonPrimitive(0)).jsonPrimitive.int],
        json.getOrDefault("data", buildJsonObject { }).jsonObject
    )

    fun json(): JsonObject {
        val json = buildJsonObject {
            put("type", JsonPrimitive(type.ordinal))
            put("data", data)
        }
        return json
    }

    override fun toString(): String {
        return Json { prettyPrint = true }.encodeToString(json())
    }

    enum class Type {
        UNDEFINED,

        REQUEST_LOGIN,
        ACCEPT_LOGIN,
        REFUSE_LOGIN,

        REQUEST_CHUNK,
        ACCEPT_CHUNK_DATA,
        REFUSE_CHUNK_DATA
    }

}