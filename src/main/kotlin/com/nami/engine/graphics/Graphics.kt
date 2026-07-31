package com.nami.engine.graphics

interface Graphics {

    val version: String

    var viewport: Area
    var polygonMode: PolygonMode
    var cullingMode: CullingMode

    fun initialize()

    fun clear()

    fun getErrorCode(): Int

}