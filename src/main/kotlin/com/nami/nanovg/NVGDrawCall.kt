package com.nami.nanovg

import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG.*
import java.awt.Color

class NVGDrawCall(private val ctx: Long) {

    private var color: NVGColor = NVGColor.create()
    private var strokeWidth: Float = 1f

    fun begin(): NVGDrawCall {
        nvgBeginPath(ctx)
        return this
    }

    fun rect(x: Float, y: Float, width: Float, height: Float): NVGDrawCall {
        nvgRect(ctx, x, y, width, height)
        return this
    }

    fun roundedRect(x: Float, y: Float, width: Float, height: Float, radius: Float): NVGDrawCall {
        nvgRoundedRect(ctx, x, y, width, height, radius)
        return this
    }

    fun circle(x: Float, y: Float, radius: Float): NVGDrawCall {
        nvgCircle(ctx, x, y, radius)
        return this
    }

    fun ellipse(x: Float, y: Float, radiusX: Float, radiusY: Float): NVGDrawCall {
        nvgEllipse(ctx, x, y, radiusX, radiusY)
        return this
    }

    fun moveTo(x: Float, y: Float): NVGDrawCall {
        nvgMoveTo(ctx, x, y)
        return this
    }

    fun lineTo(x: Float, y: Float): NVGDrawCall {
        nvgLineTo(ctx, x, y)
        return this
    }

    fun color(color: Color): NVGDrawCall {
        nvgRGBAf(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f, this.color)
        return this
    }

    fun fontFace(font: String): NVGDrawCall {
        nvgFontFace(ctx, font)
        return this
    }

    fun fontSize(size: Float): NVGDrawCall {
        nvgFontSize(ctx, size)
        return this
    }

    fun textAlign(textAlign: TextAlign): NVGDrawCall {
        nvgTextAlign(ctx, textAlign.value)
        return this
    }

    enum class TextAlign(val value: Int) {
        LEFT(NVG_ALIGN_LEFT),
        CENTER(NVG_ALIGN_CENTER),
        RIGHT(NVG_ALIGN_RIGHT),
        TOP(NVG_ALIGN_TOP),
        MIDDLE(NVG_ALIGN_MIDDLE),
        BOTTOM(NVG_ALIGN_BOTTOM),
        BASELINE(NVG_ALIGN_BASELINE)
    }

    fun text(x: Float, y: Float, text: String): NVGDrawCall {
        nvgText(ctx, x, y, text)
        return this
    }

    fun textBox(x: Float, y: Float, lineBreak: Float, text: String): NVGDrawCall {
        nvgTextBox(ctx, x, y, lineBreak, text)
        return this
    }

    fun strokeWidth(width: Float): NVGDrawCall {
        strokeWidth = width
        return this
    }

    fun draw(stroke: Boolean) {
        if (stroke) {
            nvgStrokeWidth(ctx, strokeWidth)
            nvgStrokeColor(ctx, color)
            nvgStroke(ctx)
        } else {
            nvgFillColor(ctx, color)
            nvgFill(ctx)
        }

        nvgClosePath(ctx)
    }

}