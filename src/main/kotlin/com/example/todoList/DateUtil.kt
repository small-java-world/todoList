package com.example.todoList

import java.sql.Timestamp
import java.text.SimpleDateFormat

fun toTimestamp(timestampString: String): Timestamp {
    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
    return Timestamp(simpleDateFormat.parse(timestampString).time)
}

fun timestampToString(timestamp: Timestamp): String {
    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
    return simpleDateFormat.format(timestamp)
}
