package com.nami.resources.recipe

import kotlinx.serialization.Serializable

@Serializable
data class RecipeJSON(
    val item: String,
    val variants: List<RecipeVariantJSON>
)