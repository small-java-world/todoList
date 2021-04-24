package com.example.todoList.form

data class TodoEditForm(
    var id: Long?,
    var title: String,
    var content: String,
    var limittime: String
)
