package com.nami.engine

import com.nami.engine.window.Window

interface Platform {

    val version: String
    val timeInSeconds: Double
    val isVsyncEnabled: Boolean

    fun initialize()
    fun createWindow(): Window

}