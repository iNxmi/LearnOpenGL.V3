package com.nami.model

import com.nami.texture.Texture
import org.joml.Vector3f

data class Material(
    val color: Vector3f,
    val textures: Map<Texture.Type, Texture>,
    val specularExponent: Float
)
