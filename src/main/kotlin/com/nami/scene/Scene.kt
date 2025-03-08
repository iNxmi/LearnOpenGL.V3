package com.nami.scene

interface Scene {

    fun enable()

    fun update()

    fun render()

    fun renderHUD()

    fun disable()

}