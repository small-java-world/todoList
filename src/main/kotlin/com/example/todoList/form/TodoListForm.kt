package com.example.todoList.form

import com.example.todoList.entity.Todo


data class TodoListForm(val todoList:MutableIterable<Todo>?, var searchCondTitle:String?)
