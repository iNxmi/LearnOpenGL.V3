package com.nami.resources.feature

import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.snakeToUpperCamelCase
import com.nami.world.resources.feature.Feature
import com.nami.world.resources.feature.FeatureListener
import java.nio.file.Path

class ResourceLoaderFeature : Resources<Feature>(GamePath.feature, "feature", arrayOf("json")) {

    override fun onLoad(id: String, path: Path): Feature {
        val handlerClass: Class<*> =
            try {
                Class.forName("com.nami.world.resources.feature.handlers.FeatureHandler${id.snakeToUpperCamelCase()}")
            } catch (e: Exception) {
                Class.forName("com.nami.world.resources.feature.handlers.DefaultFeatureHandler")
            }

        return Feature(
            id,
            handlerClass as Class<FeatureListener>
        )
    }

    override fun onLoadCompleted() {

    }

}