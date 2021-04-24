package com.example.todoList.form

import com.example.todoList.dto.TodoDto

data class TodoListForm(val todoList:List<TodoDto>?, var searchCondTitle:String?)
