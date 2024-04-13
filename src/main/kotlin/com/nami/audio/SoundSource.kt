package com.nami.audio

import org.lwjgl.openal.AL10.*

class SoundSource(val pointer: Int) {

    var loop: Boolean = false
        set(value) {
            alSourcei(pointer, AL_LOOPING, if (value) AL_TRUE else AL_FALSE)
            field = value
        }

    var relative: Boolean = false
        set(value) {
            alSourcei(pointer, AL_SOURCE_RELATIVE, if (value) AL_TRUE else AL_FALSE)
            field = value
        }

    var gain: Float = 0f
        set(value) {
            alSourcef(pointer, AL_GAIN, value)
            field = value
        }

    init {
        this.loop = false
        this.relative = false
        this.gain = 0f

        alSource3f(pointer, AL_POSITION, 0f, 0f, 0f)
    }

    fun play(buffer: SoundBuffer): SoundSource {
        alSourcei(pointer, AL_BUFFER, buffer.pointer)
        alSourcePlay(pointer)
        return this
    }

    fun isPlaying(): Boolean {
        return alGetSourcei(pointer, AL_SOURCE_STATE) == AL_PLAYING
    }

    fun pause(): SoundSource {
        alSourcePause(pointer)
        return this
    }

    fun stop(): SoundSource {
        alSourceStop(pointer)
        return this
    }

}