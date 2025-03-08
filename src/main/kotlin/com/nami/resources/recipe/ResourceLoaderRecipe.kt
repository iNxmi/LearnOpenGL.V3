package com.nami.resources.recipe

import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.world.resources.recipe.Recipe
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderRecipe : Resources<Recipe>(GamePath.recipe, "recipe", arrayOf("json")) {
    override fun onLoad(id: String, path: Path): Recipe {
        val jsonString = Files.readString(path)
        val json = Json.decodeFromString<Recipe.JSON>(jsonString)

        return json.create(id)
    }

    override fun onLoadCompleted() {

    }

}