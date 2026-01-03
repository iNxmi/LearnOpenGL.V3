package com.nami.logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase

class HighlightCompositeConverter : ForegroundCompositeConverterBase<ILoggingEvent>() {

    override fun getForegroundColorCode(event: ILoggingEvent) = when (event.level) {
        Level.DEBUG -> ANSIConstants.CYAN_FG
        Level.ERROR -> ANSIConstants.RED_FG
        Level.WARN -> ANSIConstants.YELLOW_FG
        else -> ANSIConstants.DEFAULT_FG
    }

}