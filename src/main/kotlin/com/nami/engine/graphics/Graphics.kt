package com.nami.engine.graphics

interface Graphics {

    val version: String

    var viewport: Area

    fun initialize()

    fun clear()

    fun getErrorCode(): Int

}