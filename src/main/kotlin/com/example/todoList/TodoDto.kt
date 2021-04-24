package com.example.todoList

import java.sql.Timestamp

data class TodoDto (
    var id: Long?,
    var title: String?,
    var content: String?,
    var limittime: Timestamp?,
    var limittimeLabel: String?
)