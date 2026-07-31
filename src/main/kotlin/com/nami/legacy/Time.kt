package com.nami

import com.nami.legacy.Game

class Time {

    var seconds = 0f
        private set
    var delta = 0f
        private set

    var scale = 1.0f

    fun update() {
        delta = Game.DELTA_TIME * scale
        seconds += delta
    }

}