package com.nami

import com.nami.game.client.Client

class Time {

    var seconds = 0f
        private set
    var delta = 0f
        private set

    var scale = 1.0f

    fun update() {
        delta = Client.DELTA_TIME * scale
        seconds += delta
    }

}