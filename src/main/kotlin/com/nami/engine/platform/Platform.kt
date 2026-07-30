package com.nami.engine.platform

import com.nami.engine.platform.window.Window

interface Platform {

    val version: String
    val timeInSeconds: Double

    fun initialize()
    fun createWindow(width: Int, height: Int, title: String = ""): Window

}