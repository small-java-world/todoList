package com.example.todoList.service

import com.example.todoList.TestBase
import com.example.todoList.repository.TodoRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat

class TodoServiceTest : TestBase(){

    lateinit var todoService: TodoService

    @MockK
    private lateinit var todoRepository: TodoRepository

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        todoService = TodoService(todoRepository)
    }

    @Test
    fun `testFindTodoList`() {
        val mockResultTodoList = generateMockResultTotoList(false)
        every { todoRepository.findAll() } returns mockResultTodoList

        val todoResultList = todoService.findTodoList()

        Assertions.assertThat(todoResultList).extracting("id", "title", "content", "limittime")
            .containsExactly(
                Tuple.tuple(mockResultTodoList[0].id, mockResultTodoList[0].title, mockResultTodoList[0].content, mockResultTodoList[0].limittime),
                Tuple.tuple(mockResultTodoList[1].id, mockResultTodoList[1].title, mockResultTodoList[1].content, mockResultTodoList[1].limittime)
            )

        verify(exactly = 1) { todoRepository.findAll() }
    }

    @Test
    fun `testFindTodoListByTitle`() {
        val mockResultTodoList = generateMockResultTotoList(true)
        val searchCondTitle = "titleCond"

        every { todoRepository.findByTitle("%$searchCondTitle%") } returns mockResultTodoList

        val todoResultList = todoService.findTodoListByTitle(searchCondTitle)

        Assertions.assertThat(todoResultList).extracting("id", "title", "content", "limittime")
            .containsExactly(
                Tuple.tuple(mockResultTodoList[0].id, mockResultTodoList[0].title, mockResultTodoList[0].content, mockResultTodoList[0].limittime),
                Tuple.tuple(mockResultTodoList[1].id, mockResultTodoList[1].title, mockResultTodoList[1].content, mockResultTodoList[1].limittime)
            )

        verify(exactly = 1) { todoRepository.findByTitle("%$searchCondTitle%") }
    }
}