package com.example.todoList.controller

import com.example.todoList.entity.Todo
import com.example.todoList.form.TodoListForm
import com.example.todoList.service.TodoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController @Autowired constructor(private val todoService: TodoService) {
    @PostMapping("/search")
    fun search(todoListForm: TodoListForm): MutableIterable<Todo> {
        return todoService.findTodoListByTitle(todoListForm.searchCondTitle)
    }
}