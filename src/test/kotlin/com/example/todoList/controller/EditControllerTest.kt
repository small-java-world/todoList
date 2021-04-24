package com.example.todoList.controller

import com.example.todoList.controller.ControllerTestBase
import com.example.todoList.dto.TodoDto
import com.example.todoList.entity.Todo
import com.example.todoList.service.TodoService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap


@ExtendWith(SpringExtension::class)
@WebMvcTest(EditController::class)
class EditControllerTest : ControllerTestBase() {
    companion object {
        private const val LIMIT_TIME = "2021/04/20"
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var todoService: TodoService

    @Test
    fun `inputForm`() {
        val mvcResult = mockMvc.perform(get("/inputForm"))
            .andExpect(view().name("/inputForm"))
            .andExpect(status().isOk)
            .andReturn()

        assertFileEquals("inputForm.txt",
            mvcResult.response.contentAsString)
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `regist`(isSuccess:Boolean) {
        val todo = Todo(null, "todo5", "5content", toTimestamp(LIMIT_TIME))

        every { todoService.save(todoDto = any<TodoDto>()) } returns isSuccess

        val params = LinkedMultiValueMap<String, String>()
        params.add("title", todo.title)
        params.add("content", todo.content)
        params.add("limittimeText", LIMIT_TIME)

        if(isSuccess) {
            mockMvc.perform(post("/regist").param("title", todo.title, "content", todo.content))
                .andExpect(status().isFound)
                .andExpect(redirectedUrl("/top/list"))
                .andReturn()
        }
        else {
            val mvcResult = mockMvc.perform(post("/regist").params(params))
                .andExpect(status().isOk)
                .andExpect(view().name("/inputForm"))
                .andExpect(model().attribute("errorMessage", "登録失敗")).andReturn()

            val todoDto = mvcResult.modelAndView!!.modelMap["todoDto"] as TodoDto
            assertEquals(todo.title, todoDto.title)
            assertEquals(todo.content, todoDto.content)
            assertEquals(LIMIT_TIME, todoDto.limittimeText)
            //limittimeはモックの中の話なので検証できない。
            //assertEquals(todo.limittime, todoDto.limittime)
        }

        verify(exactly = 1) { todoService.save(todoDto = any<TodoDto>()) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `regist using flashAttr`(isSuccess:Boolean) {
        val requestTodoDto = TodoDto(null, "todo5", "5content", null, LIMIT_TIME)

        every { todoService.save(requestTodoDto) } returns isSuccess

        if(isSuccess) {
            mockMvc.perform(post("/regist").flashAttr("todoDto",requestTodoDto))
                .andExpect(status().isFound)
                .andExpect(redirectedUrl("/top/list"))
                .andReturn()
        }
        else {
            val mvcResult = mockMvc.perform(post("/regist").flashAttr("todoDto",requestTodoDto))
                .andExpect(status().isOk)
                .andExpect(view().name("/inputForm"))
                .andExpect(model().attribute("errorMessage", "登録失敗")).andReturn()

            val todoDto = mvcResult.modelAndView!!.modelMap["todoDto"] as TodoDto
            assertEquals(requestTodoDto, todoDto)
        }

        verify(exactly = 1) { todoService.save(requestTodoDto) }
    }

}