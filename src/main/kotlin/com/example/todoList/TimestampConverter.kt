package com.example.todoList

import org.springframework.core.convert.converter.Converter
import java.sql.Timestamp

class TimestampConverter : Converter<String, Timestamp> {
    /**
     * yyyy/MM/ddの形式の文字列wをTimestampに変換します。
     *
     * @param yyyy/MM/ddの形式の文字列
     * @return Timestamp
     */
    override fun convert(timestampString:String) : Timestamp {
        return toTimestamp(timestampString)
    }
}