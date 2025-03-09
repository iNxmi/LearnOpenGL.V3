package com.nami.client.resources.feature

import com.nami.client.resources.GamePath
import com.nami.client.resources.Resources
import com.nami.client.snakeToUpperCamelCase
import com.nami.client.world.resources.feature.Feature
import com.nami.client.world.resources.feature.FeatureListener
import java.nio.file.Path

class ResourceLoaderFeature : Resources<Feature>(GamePath.feature, "feature", arrayOf("json")) {

    override fun onLoad(id: String, path: Path): Feature {
        val handlerClass: Class<*> =
            try {
                Class.forName("com.nami.client.world.resources.feature.handlers.FeatureHandler${id.snakeToUpperCamelCase()}")
            } catch (e: Exception) {
                Class.forName("com.nami.client.world.resources.feature.handlers.DefaultFeatureHandler")
            }

        return Feature(
            id,
            handlerClass as Class<FeatureListener>
        )
    }

    override fun onLoadCompleted() {

    }

}