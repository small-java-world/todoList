package com.example.todoList.controller

import com.example.todoList.TestBase
import com.example.todoList.entity.Todo
import com.example.todoList.service.TodoService
import com.example.todoList.toTimestamp
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap
import java.util.*


@ExtendWith(SpringExtension::class)
@WebMvcTest(EditController::class)
class EditControllerTest : TestBase() {
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
        val requestTodo = Todo(null, "todo5", "5content", toTimestamp(LIMIT_TIME))

        every { todoService.save(todo = any<Todo>()) } returns isSuccess

        val params = LinkedMultiValueMap<String, String>()
        params.add("title", requestTodo.title)
        params.add("content", requestTodo.content)
        params.add("limittime", LIMIT_TIME)

        if(isSuccess) {
            val mvcResult = mockMvc.perform(post("/regist").params(params))
                .andExpect(status().isFound)
                .andExpect(redirectedUrl("/top/list"))
                .andReturn()

            //EditControllerで@SessionAttributes("todo")を指定しているのでTopControllerに遷移するとmodelMapにtodoは含まれない
            assertFalse(mvcResult.modelAndView!!.modelMap.containsAttribute("todo"))
        }
        else {
            val mvcResult = mockMvc.perform(post("/regist").params(params))
                .andExpect(status().isOk)
                .andExpect(view().name("/inputForm"))
                .andExpect(model().attribute("errorMessage", "登録失敗")).andReturn()

            val todo = mvcResult.modelAndView!!.modelMap["todo"] as Todo
            assertEquals(requestTodo.title, todo.title)
            assertEquals(todo.content, todo.content)
            assertEquals(requestTodo.limittime, todo.limittime)
        }

        verify(exactly = 1) { todoService.save(todo = any<Todo>()) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `regist using flashAttr`(isSuccess:Boolean) {
        val requesttodo = Todo(null, "todo5", "5content", toTimestamp(LIMIT_TIME))

        every { todoService.save(requesttodo) } returns isSuccess

        if(isSuccess) {
            val mvcResult = mockMvc.perform(post("/regist").flashAttr("todo",requesttodo))
                .andExpect(status().isFound)
                .andExpect(redirectedUrl("/top/list"))
                .andReturn()
        }
        else {
            val mvcResult = mockMvc.perform(post("/regist").flashAttr("todo",requesttodo))
                .andExpect(status().isOk)
                .andExpect(view().name("/inputForm"))
                .andExpect(model().attribute("errorMessage", "登録失敗")).andReturn()

            val todo = mvcResult.modelAndView!!.modelMap["todo"] as Todo
            assertEquals(requesttodo, todo)
        }

        verify(exactly = 1) { todoService.save(requesttodo) }
    }


    @Test
    fun `editForm`() {
        val requestTodo = Todo(100, null, null, null)
        val findByIdResult : Optional<Todo> = Optional.of(Todo(100, "todo100", "100content", toTimestamp(LIMIT_TIME)))

        every { todoService.findById(requestTodo.id!!) } returns findByIdResult

        val mvcResult = mockMvc.perform(get("/editForm").flashAttr("todo",requestTodo))
            .andExpect(view().name("/inputForm"))
            .andExpect(status().isOk)
            .andReturn()

        val todo = mvcResult.modelAndView!!.modelMap["todo"] as Todo
        assertEquals(findByIdResult.get(), todo)

        assertFileEquals("editForm.txt",
            mvcResult.response.contentAsString)
    }

    @ParameterizedTest
    @CsvSource(value = ["true,true", "true,false","false,true","false,false"])
    fun `regist update or create using flashAttr`(isUpdate:Boolean, isSuccess:Boolean) {
        val requestTodo = Todo(if(isUpdate) 1 else null, "todo5", "5content", toTimestamp(LIMIT_TIME))

        every { todoService.save(requestTodo) } returns isSuccess

        if(isSuccess) {
            val mvcResult = mockMvc.perform(post("/regist").flashAttr("todo",requestTodo))
                .andExpect(status().isFound)
                .andExpect(redirectedUrl("/top/list"))
                .andReturn()

            //EditControllerで@SessionAttributes("todo")を指定しているのでTopControllerに遷移するとmodelMapにtodoは含まれない
            assertFalse(mvcResult.modelAndView!!.modelMap.containsAttribute("todo"))
        }
        else {
            val expectedErrorMessage = if(isUpdate) "更新失敗" else "登録失敗"
            val mvcResult = mockMvc.perform(post("/regist").flashAttr("todo",requestTodo))
                .andExpect(status().isOk)
                .andExpect(view().name("/inputForm"))
                .andExpect(model().attribute("errorMessage", expectedErrorMessage)).andReturn()

            val todo = mvcResult.modelAndView!!.modelMap["todo"] as Todo
            assertEquals(requestTodo, todo)
        }

        verify(exactly = 1) { todoService.save(requestTodo) }
    }

}