package com.nami.engine.platform

import com.nami.engine.platform.window.Window

interface Platform {

    val version: String
    val timeInSeconds: Double
    val isVsyncEnabled: Boolean

    fun initialize()
    fun createWindow(): Window

}