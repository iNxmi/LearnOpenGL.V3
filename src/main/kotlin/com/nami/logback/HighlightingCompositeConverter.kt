package com.nami.logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase


class HighlightingCompositeConverter : ForegroundCompositeConverterBase<ILoggingEvent>() {

    override fun getForegroundColorCode(event: ILoggingEvent): String {
        val level: Level = event.level
        return when (level.toInt()) {
            Level.ERROR_INT -> ANSIConstants.RED_FG
            Level.WARN_INT -> ANSIConstants.YELLOW_FG
            else -> ANSIConstants.DEFAULT_FG
        }
    }

}