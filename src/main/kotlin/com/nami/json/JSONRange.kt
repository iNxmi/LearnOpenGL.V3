package com.nami.json

import kotlinx.serialization.Serializable

@Serializable
data class JSONRange<T>(
    val min: T,
    val max: T
)