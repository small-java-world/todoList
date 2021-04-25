package com.example.todoList.controller

import com.example.todoList.TestBase
import com.example.todoList.service.TodoService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@ExtendWith(SpringExtension::class)
@WebMvcTest(DownloadController::class)
class DownloadControllerTest : TestBase() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var todoService: TodoService

    @Test
    fun `download`() {
        //todoService.findTodoList()でmockResultTodoListが返却されるようにする。
        every { todoService.getDownloadCsvData() } returns "dummyCavData"

        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/download"))
            .andExpect(status().isOk)
            .andExpect(content().string("dummyCavData"))
            .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=utf-8''TODO%E3%83%80%E3%82%A6%E3%83%B3%E3%83%AD%E3%83%BC%E3%83%89.csv"))
            .andReturn()

        //HttpHeaders.CONTENT_DISPOSITIONの検証が直感的じゃないのでデコードして検証
        val contentDisposition = mvcResult.response.getHeader(HttpHeaders.CONTENT_DISPOSITION)
        assertEquals("attachment; filename*=utf-8''TODOダウンロード.csv", URLDecoder.decode(contentDisposition, StandardCharsets.UTF_8.name()))

        verify(exactly = 1) { todoService.getDownloadCsvData() }
    }

}