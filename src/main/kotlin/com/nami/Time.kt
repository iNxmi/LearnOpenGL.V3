package com.nami

import kotlinx.serialization.Serializable

@Serializable
class Time {

    var time = 0f
        private set
    var delta = 0f
        private set

    var scale = 1.0f

    fun update() {
        delta = Game.DELTA_TIME * scale
        time += delta
    }

}