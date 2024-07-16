package com.nami.resources

abstract class Resource(prefix: String, val id: String) {

    val absoluteID: String = "$prefix.$id"

    fun language(component: String): String {
        return Resources.LANGUAGE.resolve("$absoluteID.$component")
    }

}