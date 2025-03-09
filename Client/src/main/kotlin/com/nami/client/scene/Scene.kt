package com.nami.client.scene

interface Scene {

    fun enable()

    fun update()

    fun render()

    fun renderHUD()

    fun disable()

}