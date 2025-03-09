package com.nami.client

class Time {

    var time = 0f
        private set
    var delta = 0f
        private set

    var scale = 1.0f

    fun update() {
        delta = Client.DELTA_TIME * scale
        time += delta
    }

}