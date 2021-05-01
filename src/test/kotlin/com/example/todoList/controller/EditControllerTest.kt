package com.example.todoList.controller

import com.example.todoList.TestBase
import com.example.todoList.entity.Todo
import com.example.todoList.service.TodoService
import com.example.todoList.toTimestamp
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

        // 期待値ファイル src/test/resources/com/example/todoList/controller/inputForm.txt
        // とレスポンスのテキストを比較
        assertFileEquals(
            "inputForm.txt",
            mvcResult.response.contentAsString
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `save test`(isSuccess: Boolean) {
        val requestTodo = Todo(null, "todo5", "5content", toTimestamp(LIMIT_TIME))

        //todoServiceのsave(todo)が呼び出されるとisSuccessの値が返却されるように振る舞いを指定
        every { todoService.save(todo = any<Todo>()) } returns isSuccess

        //リクエストパラメーターを準備
        val params = LinkedMultiValueMap<String, String>()
        params.add("title", requestTodo.title)
        params.add("content", requestTodo.content)
        params.add("limittime", LIMIT_TIME)

        if (isSuccess) {
            //todoService.saveの結果がtrueの場合は/top/listへリダイレクトされることを確認
            mockMvc.perform(post("/save").params(params))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/top/list"))
                .andReturn()
        } else {
            //todoService.saveの結果がfalseの場合は/inputFormががviewとなり、modelにerrorMessage="登録失敗"が設定されている事を確認
            val mvcResult = mockMvc.perform(post("/save").params(params))
                .andExpect(status().isOk)
                .andExpect(view().name("/inputForm"))
                .andExpect(model().attribute("errorMessage", "登録失敗")).andReturn()

            //リクエストパラメーターがtodoにマッピングされていることを確認
            val todo = mvcResult.modelAndView!!.modelMap["todo"] as Todo
            assertEquals(requestTodo.title, todo.title)
            assertEquals(requestTodo.content, todo.content)
            assertEquals(requestTodo.limittime, todo.limittime)
        }

        //todoService.saveが一度だけ呼び出されたことを確認
        verify(exactly = 1) { todoService.save(todo = any<Todo>()) }
    }


    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun `save test using flashAttr`(isSuccess: Boolean) {
        val requestTodo = Todo(null, "todo5", "5content", toTimestamp(LIMIT_TIME))

        every { todoService.save(requestTodo) } returns isSuccess

        if (isSuccess) {
            val mvcResult = mockMvc.perform(post("/save").flashAttr("todo", requestTodo))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/top/list"))
                .andReturn()
        } else {
            val mvcResult = mockMvc.perform(post("/save").flashAttr("todo", requestTodo))
                .andExpect(status().isOk)
                .andExpect(view().name("/inputForm"))
                .andExpect(model().attribute("errorMessage", "登録失敗")).andReturn()

            val todo = mvcResult.modelAndView!!.modelMap["todo"] as Todo
            assertEquals(requestTodo, todo)
        }

        verify(exactly = 1) { todoService.save(requestTodo) }
    }

    @Test
    fun `editForm test`() {
        val requestTodo = Todo(100, null, null, null)
        val findByIdResult: Optional<Todo> = Optional.of(Todo(100, "todo100", "100content", toTimestamp(LIMIT_TIME)))

        every { todoService.findById(requestTodo.id!!) } returns findByIdResult

        val mvcResult = mockMvc.perform(get("/editForm").flashAttr("todo", requestTodo))
            .andExpect(view().name("/inputForm"))
            .andExpect(status().isOk)
            .andReturn()

        val todo = mvcResult.modelAndView!!.modelMap["todo"] as Todo
        assertEquals(findByIdResult.get(), todo)

        verify(exactly = 1) { todoService.findById(requestTodo.id!!) }

        assertFileEquals(
            "editForm.txt",
            mvcResult.response.contentAsString
        )
    }

    @Test
    fun `todoがバリデートされている事を確認するだけ`() {
        //TodoTestでバリデーションのテストのバリエーションを考慮しているのでエラーになる事だけ確認
        val requestTodo = Todo(null, "", "5content", toTimestamp(LIMIT_TIME))
        val resultActions = mockMvc.perform(post("/save").flashAttr("todo", requestTodo))
        resultActions.andExpect(model().errorCount(1));
    }

}