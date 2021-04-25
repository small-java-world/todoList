package com.example.todoList

import org.springframework.core.convert.converter.Converter
import java.sql.Timestamp

class TimestampConverter : Converter<String, Timestamp> {
    /**
     * Override the convert method
     * @param date
     * @return
     */
    override fun convert(timestampString:String) : Timestamp {
        return toTimestamp(timestampString)
    }
}