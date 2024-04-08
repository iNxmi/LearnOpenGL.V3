package com.nami.model

import com.nami.texture.Texture
import com.nami.texture.TextureType
import org.joml.Vector3f

data class Material(
    val color: Vector3f,
    val textures: Map<TextureType, Texture>,
    val specularExponent: Float
)
