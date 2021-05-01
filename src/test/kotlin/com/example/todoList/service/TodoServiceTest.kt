package com.example.todoList.service

import com.example.todoList.TestBase
import com.example.todoList.controller.EditControllerTest
import com.example.todoList.entity.Todo
import com.example.todoList.repository.TodoRepository
import com.example.todoList.toTimestamp
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.text.SimpleDateFormat
import java.util.*

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

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `test save todo`(isSuccess:Boolean) {
        val todo = Todo(1, "title", "content", toTimestamp("2021/03/20"))

        //モックであるtodoRepository#saveの結果を指定
        if(isSuccess) {
            every { todoRepository.save(todo) } returns todo
        }else {
            every { todoRepository.save(todo) }.throws(Exception())
        }

        assertEquals(isSuccess, todoService.save(todo))

        //todoRepository.saveの呼び出し回数を確認
        verify(exactly = 1) { todoRepository.save(todo) }
    }

    @Test
    fun `testFindById`() {
        val id = 1111
        val todo: Optional<Todo> = Optional.of(Todo(id, "todo100", "100content", toTimestamp(
            "2021/03/20"
        )))

        every { todoRepository.findById(id) } returns todo

        val findByIdResult = todoService.findById(id)

        assertEquals(todo,findByIdResult )
        verify(exactly = 1) { todoRepository.findById(id) }
    }
}