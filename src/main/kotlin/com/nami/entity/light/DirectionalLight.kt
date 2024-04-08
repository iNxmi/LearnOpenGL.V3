package com.nami.entity.light

import com.nami.entity.Entity
import org.joml.Vector3f

data class DirectionalLight(
    val direction: Vector3f = Vector3f(-1f, -1f, -1f).normalize(),
    val color: Vector3f = Vector3f(1f)
) : Entity()