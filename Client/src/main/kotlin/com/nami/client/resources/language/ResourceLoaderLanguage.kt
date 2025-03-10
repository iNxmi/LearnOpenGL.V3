package com.nami.client.resources.language

import com.nami.client.resources.GamePath
import com.nami.client.resources.Resources
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderLanguage : Resources<com.nami.client.resources.language.Language>(GamePath.language, "language", arrayOf("json")) {

    private var selected: com.nami.client.resources.language.Language? = null

    override fun onLoad(id: String, path: Path): com.nami.client.resources.language.Language {
        val jsonString = Files.readString(path)
        val map = Json.decodeFromString<MutableMap<String, String>>(jsonString)

        return com.nami.client.resources.language.Language(id, map)
    }

    override fun onLoadCompleted() {
        select("english")
    }

    fun select(id: String): Boolean {
        if (!map.containsKey(id))
            return false

        selected = get(id)
        return true
    }

    fun resolve(id: String): String {
        return selected?.resolve(id) ?: "NO LANGUAGE SELECTED"
    }

}