package com.nami.nanovg

import com.nami.constants.GamePaths
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.*
import org.lwjgl.system.MemoryUtil
import kotlin.io.path.pathString

class NVGManager {

    companion object {

        @JvmStatic
        var ctx = 0L

        @JvmStatic
        fun init() {
            ctx = nvgCreate(NVG_ANTIALIAS or NVG_STENCIL_STROKES)
            if (ctx == MemoryUtil.NULL)
                throw RuntimeException("Failed to create NanoVG")
        }

        @JvmStatic
        fun loadFont(name: String) {
            nvgCreateFont(ctx, name, GamePaths.fonts.resolve("$name.ttf").pathString)
        }

        @JvmStatic
        fun delete() {
            nvgDelete(ctx)
        }

        @JvmStatic
        fun beginFrame(width: Float, height: Float, devicePixelRatio: Float) {
            nvgBeginFrame(ctx, width, height, devicePixelRatio)
        }

        @JvmStatic
        fun endFrame() {
            nvgEndFrame(ctx)
        }

        @JvmStatic
        fun draw(): NVGDrawCall {
            return NVGDrawCall(ctx).begin()
        }

    }

}