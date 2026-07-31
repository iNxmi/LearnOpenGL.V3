package com.nami.engine.graphics

import org.joml.Vector4f

interface Graphics {

    val version: String

    var viewport: Area
    var polygonMode: PolygonMode
    var cullingMode: CullingMode

    var clearColor: Vector4f

    fun initialize()

    fun clear()

    fun getErrorCode(): Int

}