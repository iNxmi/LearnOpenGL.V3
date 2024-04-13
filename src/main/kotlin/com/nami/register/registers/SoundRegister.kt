package com.nami.register.registers

import com.nami.audio.SoundBuffer
import com.nami.audio.WaveData
import com.nami.constants.GamePaths
import com.nami.register.Register
import org.lwjgl.openal.AL10.*
import kotlin.io.path.pathString


class SoundRegister : Register<String, SoundBuffer>() {
    override fun load(key: String): SoundBuffer {
        val pointer = alGenBuffers()

        val wav = WaveData.create(GamePaths.sounds.resolve("$key.wav").pathString)!!
        alBufferData(pointer, wav.format, wav.data, wav.samplerate)

        return SoundBuffer(pointer)
    }

}