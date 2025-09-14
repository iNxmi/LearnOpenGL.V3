package com.nami.easing

import com.google.common.collect.Range
import com.google.common.collect.TreeRangeMap
import kotlinx.serialization.Serializable
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

@Serializable
data class EasingExpression(
    val segments: List<EasingSegment>
) {

    fun toEasingFunction(): EasingFunction {
        val map = TreeRangeMap.create<Float, Expression>()
        segments.forEach { json ->
            val expression = ExpressionBuilder(json.equation)
                .variables("t")
                .build()

            map.put(Range.closed(json.range.start, json.range.endInclusive), expression)
        }

        return EasingFunction(map)
    }

}