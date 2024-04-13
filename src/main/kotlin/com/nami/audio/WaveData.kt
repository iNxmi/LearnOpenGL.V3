package com.nami.audio

import org.lwjgl.BufferUtils
import org.lwjgl.openal.AL10
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.UnsupportedAudioFileException

class WaveData private constructor(private val audioStream: AudioInputStream?) {

    val format: Int
    val samplerate: Int
    val totalBytes: Int
    val bytesPerFrame: Int
    val data: ByteBuffer

    private val dataArray: ByteArray

    init {
        val audioFormat = audioStream!!.format
        format = getOpenAlFormat(audioFormat.channels, audioFormat.sampleSizeInBits)
        this.samplerate = audioFormat.sampleRate.toInt()
        this.bytesPerFrame = audioFormat.frameSize
        this.totalBytes = (audioStream.frameLength * bytesPerFrame).toInt()
        this.data = BufferUtils.createByteBuffer(totalBytes)
        this.dataArray = ByteArray(totalBytes)
        loadData()
    }

    protected fun dispose() {
        try {
            audioStream!!.close()
            data.clear()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadData(): ByteBuffer {
        try {
            val bytesRead = audioStream!!.read(dataArray, 0, totalBytes)
            data.clear()
            data.put(dataArray, 0, bytesRead)
            data.flip()
        } catch (e: IOException) {
            e.printStackTrace()
            System.err.println("Couldn't read bytes from audio stream!")
        }
        return data
    }


    companion object {
        fun create(file: String): WaveData {
            val stream = File(file).inputStream()

            val bufferedInput: InputStream = BufferedInputStream(stream)
            var audioStream: AudioInputStream? = null
            try {
                audioStream = AudioSystem.getAudioInputStream(bufferedInput)
            } catch (e: UnsupportedAudioFileException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val wavStream = WaveData(audioStream)
            return wavStream
        }


        private fun getOpenAlFormat(channels: Int, bitsPerSample: Int): Int {
            return if (channels == 1) {
                if (bitsPerSample == 8) AL10.AL_FORMAT_MONO8 else AL10.AL_FORMAT_MONO16
            } else {
                if (bitsPerSample == 8) AL10.AL_FORMAT_STEREO8 else AL10.AL_FORMAT_STEREO16
            }
        }
    }
}