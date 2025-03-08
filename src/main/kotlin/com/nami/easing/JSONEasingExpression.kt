package com.nami.easing

import com.google.common.collect.Range
import com.google.common.collect.TreeRangeMap
import kotlinx.serialization.Serializable
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

@Serializable
data class JSONEasingExpression(
    val segments: List<JSONEasingSegment>
) {

    fun toEasingFunction(): EasingFunction {
        val segmentsTree = TreeRangeMap.create<Float, Expression>()
        segments.forEach { json ->
            val expression = ExpressionBuilder(json.equation)
                .variables("t")
                .build()

            segmentsTree.put(Range.closed(json.range.min, json.range.max), expression)
        }

        return EasingFunction(segmentsTree)
    }

}