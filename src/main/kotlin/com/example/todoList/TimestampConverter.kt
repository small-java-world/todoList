package com.example.todoList

import org.springframework.core.convert.converter.Converter
import java.sql.Timestamp
import java.text.SimpleDateFormat

class TimestampConverter : Converter<String, Timestamp> {
    companion object {
        const val LIMIT_TIME_FORMAT = "yyyy/MM/dd"
    }

    /**
     * Override the convert method
     * @param date
     * @return
     */
    override fun convert(dateString:String) : Timestamp {
        val dateFormat = SimpleDateFormat(LIMIT_TIME_FORMAT)
        return Timestamp(dateFormat.parse(dateString).time)
    }
}