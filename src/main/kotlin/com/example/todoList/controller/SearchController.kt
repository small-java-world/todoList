package com.example.todoList.controller

import com.example.todoList.dto.TodoDto
import com.example.todoList.form.TodoListForm
import com.example.todoList.service.TodoService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController @Autowired constructor(private val todoService: TodoService) {
    val logger: Logger = LoggerFactory.getLogger(SearchController::class.java)

    @PostMapping("/search")
    fun search(todoListForm: TodoListForm): List<TodoDto> {
        return todoService.findTodoListByTitle(todoListForm.searchCondTitle)
    }
}