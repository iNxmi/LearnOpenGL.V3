package com.nami.world.entity

import org.joml.Vector3f

interface EntityController {

    fun move(direction: Vector3f, distance: Vector3f)

}