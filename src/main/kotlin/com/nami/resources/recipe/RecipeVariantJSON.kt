package com.nami.resources.recipe

import kotlinx.serialization.Serializable

@Serializable
data class RecipeVariantJSON(
    val workstations: Array<String>? = null,
    val amount: Int = 1,
    val duration: Float,
    val ingredients: Map<String, Int>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecipeVariantJSON

        if (workstations != null) {
            if (other.workstations == null) return false
            if (!workstations.contentEquals(other.workstations)) return false
        } else if (other.workstations != null) return false
        if (amount != other.amount) return false
        if (duration != other.duration) return false
        if (ingredients != other.ingredients) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workstations?.contentHashCode() ?: 0
        result = 31 * result + amount
        result = 31 * result + duration.hashCode()
        result = 31 * result + ingredients.hashCode()
        return result
    }

}
