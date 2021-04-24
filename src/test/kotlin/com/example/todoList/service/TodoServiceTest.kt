package com.example.todoList.service

import com.example.todoList.TestBase
import com.example.todoList.repository.TodoRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach

class TodoServiceTest : TestBase(){

    lateinit var todoService: TodoService

    @MockK
    private lateinit var todoRepository: TodoRepository

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        todoService = TodoService(todoRepository)
    }
}