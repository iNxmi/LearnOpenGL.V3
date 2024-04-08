package com.nami.entity.light

import com.nami.entity.Entity
import com.nami.model.ModelManager
import org.joml.Vector3f

class PointLight(
    val position: Vector3f = Vector3f(),
    val color: Vector3f = Vector3f(1f),
    val attenuation: Vector3f = Vector3f(1f, 0.09f, 0.032f)
) : Entity(ModelManager.get("sphere"))