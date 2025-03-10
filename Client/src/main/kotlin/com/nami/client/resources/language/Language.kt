package com.nami.client.resources.language

import mu.KotlinLogging

class Language(
    id: String,
    val map: MutableMap<String, String>
) : com.nami.client.resources.language.ResourceLanguage(id) {

    private val log = KotlinLogging.logger {  }

    fun resolve(id: String): String {
        if(!map.containsKey(id)) {
            log.warn { "Couldn't find '$id' in language '${this.id}'" }
            map[id] = id
        }

        return map[id]!!
    }

}