package com.example.todoList.controller

import com.example.todoList.controller.ControllerTestBase
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.text.SimpleDateFormat


@ExtendWith(SpringExtension::class)
@WebMvcTest(SearchController::class)
class SearchControllerTest : ControllerTestBase() {
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
        val mockResultTodoDtoList = generateMockResultTotoDtoList(true)

        val searchCondTitleValue = "searchCondTitleValue"

        every { todoService.findTodoListByTitle(searchCondTitleValue) } returns mockResultTodoDtoList

        val dateFormat = SimpleDateFormat("yyyy/MM/dd")

        mockMvc.perform(post("/search").param("searchCondTitle", searchCondTitleValue))
                .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(mockResultTodoDtoList[0].id))
            .andExpect(jsonPath("$[0].title").value(mockResultTodoDtoList[0].title))
            .andExpect(jsonPath("$[0].content").value(mockResultTodoDtoList[0].content))
            .andExpect(jsonPath("$[0].limittimeText").value(mockResultTodoDtoList[0].limittimeText))
            .andExpect(jsonPath("$[1].id").value(mockResultTodoDtoList[1].id))
            .andExpect(jsonPath("$[1].title").value(mockResultTodoDtoList[1].title))
            .andExpect(jsonPath("$[1].content").value(mockResultTodoDtoList[1].content))
            .andExpect(jsonPath("$[1].limittimeText").value(mockResultTodoDtoList[1].limittimeText))

        verify(exactly = 1) { todoService.findTodoListByTitle(searchCondTitleValue) }
    }

}