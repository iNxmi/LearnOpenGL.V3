package com.nami.easing

import com.google.common.collect.RangeMap
import net.objecthunter.exp4j.Expression

class EasingFunction(
    private val segments: RangeMap<Float, Expression>
) {

    fun evaluate(t: Float): Float {
        val expression = segments.get(t) ?: return 0.0f
        return expression
            .setVariable("t", t.toDouble())
            .evaluate().toFloat()
    }

}