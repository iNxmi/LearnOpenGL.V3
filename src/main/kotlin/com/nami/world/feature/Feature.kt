package com.nami.world.feature

import com.nami.resources.feature.ResourceFeature

class Feature(
    id: String,
    private val handlerClass: Class<FeatureListener>
) : ResourceFeature(id) {

    fun create(): Instance {
        val handler = handlerClass.getDeclaredConstructor().newInstance()
        return Instance(this, handler)
    }

    class Instance(
        val template: Feature,
        val handler: FeatureListener
    )

}