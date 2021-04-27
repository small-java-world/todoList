package com.example.todoList.controller

import com.example.todoList.TestBase
import com.example.todoList.service.TodoService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@ExtendWith(SpringExtension::class)
@WebMvcTest(SearchController::class)
class SearchControllerTest : TestBase() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var todoService: TodoService

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun todoService() = mockk<TodoService>()
    }

    @Test
    fun `search`() {
        val mockResultTodoDtoList = generateMockResultTotoList(true)

        val searchCondTitleValue = "searchCondTitleValue"

        every { todoService.findTodoListByTitle(searchCondTitleValue) } returns mockResultTodoDtoList

        mockMvc.perform(post("/search").param("searchCondTitle", searchCondTitleValue))
                .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(mockResultTodoDtoList[0].id))
            .andExpect(jsonPath("$[0].title").value(mockResultTodoDtoList[0].title))
            .andExpect(jsonPath("$[0].content").value(mockResultTodoDtoList[0].content))
            .andExpect(jsonPath("$[0].limittime").value("2021/04/20"))
            .andExpect(jsonPath("$[0].limittimeText").value("2021/04/20"))
            .andExpect(jsonPath("$[1].id").value(mockResultTodoDtoList[1].id))
            .andExpect(jsonPath("$[1].title").value(mockResultTodoDtoList[1].title))
            .andExpect(jsonPath("$[1].content").value(mockResultTodoDtoList[1].content))
            .andExpect(jsonPath("$[1].limittime").value("2021/04/21"))
            .andExpect(jsonPath("$[1].limittimeText").value("2021/04/21"))

        verify(exactly = 1) { todoService.findTodoListByTitle(searchCondTitleValue) }
    }

}