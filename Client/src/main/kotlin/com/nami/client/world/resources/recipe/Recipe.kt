package com.nami.client.world.resources.recipe

import com.nami.client.resources.Resources.Companion.BLOCK
import com.nami.client.resources.Resources.Companion.ITEM
import com.nami.client.resources.recipe.RecipeVariantJSON
import com.nami.client.resources.recipe.ResourceRecipe
import com.nami.client.world.resources.item.Item
import kotlinx.serialization.Serializable

class Recipe(
    id: String,
    val item: Item,
    val variants: Set<RecipeVariant>
) : ResourceRecipe(id) {

    @Serializable
    data class JSON(
        val item: String,
        val variants: List<RecipeVariantJSON>
    ) {

        fun create(id: String): Recipe {
            val vari = mutableSetOf<RecipeVariant>()
            variants.forEach {
                val worktables = it.workstations?.map { blockID -> BLOCK.get(blockID) }?.toSet()
                val ingredients = it.ingredients.map { (k, v) -> Pair(ITEM.get(k), v) }.toMap()

                val variant = RecipeVariant(
                    worktables,
                    it.amount,
                    it.duration,
                    ingredients
                )

                vari.add(variant)
            }

            return Recipe(
                id,
                ITEM.get(item),
                vari
            )
        }

    }

}
