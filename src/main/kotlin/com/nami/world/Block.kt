package com.nami.world

import kotlinx.serialization.Serializable
import org.joml.Vector3f

open class Block(
    val colorTop: Vector3f = Vector3f(),
    val colorBottom: Vector3f = Vector3f(),
    val colorLeft: Vector3f = Vector3f(),
    val colorRight: Vector3f = Vector3f(),
    val colorFront: Vector3f = Vector3f(),
    val colorBack: Vector3f = Vector3f(),
) {

    @Serializable
    data class BlockTemplate(val texture: String)

}