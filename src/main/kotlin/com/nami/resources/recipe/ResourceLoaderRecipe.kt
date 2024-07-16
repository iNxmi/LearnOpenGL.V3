package com.nami.resources.recipe

import com.nami.resources.GamePath
import com.nami.resources.Resources
import com.nami.world.recipe.Recipe
import com.nami.world.recipe.RecipeVariant
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class ResourceLoaderRecipe : Resources<Recipe>(GamePath.recipe, "recipe", arrayOf("json")) {
    override fun onLoad(id: String, path: Path): Recipe {
        val jsonString = Files.readString(path)
        val json = Json.decodeFromString<RecipeJSON>(jsonString)

        val variants = mutableListOf<RecipeVariant>()
        json.variants.forEach {
            val worktables = it.workstations?.map { blockID -> BLOCK.get(blockID) }?.toTypedArray()
            val ingredients = it.ingredients.map { (k, v) -> Pair(ITEM.get(k), v) }.toMap()

            val variant = RecipeVariant(
                worktables,
                it.amount,
                it.duration,
                ingredients
            )

            variants.add(variant)
        }

        return Recipe(
            id,
            ITEM.get(json.item),
            variants.toList()
        )
    }

    override fun onLoadCompleted() {

    }

}