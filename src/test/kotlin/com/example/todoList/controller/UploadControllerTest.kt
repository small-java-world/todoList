package com.example.todoList.controller

import com.example.todoList.TestBase
import com.example.todoList.entity.Todo
import com.example.todoList.service.TodoService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.io.File
import java.nio.charset.StandardCharsets


@ExtendWith(SpringExtension::class)
@WebMvcTest(UploadController::class)
class UploadControllerTest : TestBase() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var todoService: TodoService

    @Test
    fun testUploadForm() {
        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/uploadForm"))
            .andExpect(view().name("/uploadForm"))
            .andExpect(status().isOk)
            .andReturn()

        assertFileEquals(
            "uploadForm.txt",
            mvcResult.response.contentAsString
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["true", "false"])
    fun testUpload(isSuccess: Boolean) {
        //MockMultipartFileに指定するアップロードファイルのコンテンツを作成
        val csvContent =
            "uploadTodo1,uploadTodoCont1,2021/04/20${File.separator}" +
                    "uploadTodo2,uploadTodoCont2,2021/04/21${File.separator}" +
                    "uploadTodo3,uploadTodoCont3,2021/04/22${File.separator}"

        //MockMultipartFileを作成
        val uploadedFile = MockMultipartFile(
            "uploadedFile",
            "todo.txt",
            MediaType.TEXT_PLAIN_VALUE,
            csvContent.toByteArray(StandardCharsets.UTF_8)
        )

        //todoService.saveAllの戻り値にisSuccessを設定
        every { todoService.saveAll(todoList = any<List<Todo>>()) } returns isSuccess

        if (isSuccess) {
            //todoService.saveAllが成功したときは/top/listにリダイレクトされることを確認
            mockMvc.perform(multipart("/upload").file(uploadedFile))
                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("/top/list"))
        } else {
            //todoService.saveAllが失敗したときはerrorMessage="アップロード失敗"がmodelに登録され、/uploadFormに遷移することを確認
            mockMvc.perform(multipart("/upload").file(uploadedFile))
                .andExpect(status().isOk)
                .andExpect(view().name("/uploadForm"))
                .andExpect(model().attribute("errorMessage", "アップロード失敗")).andReturn()
        }

        //todoService.saveAllが一回だけ呼ばれることを確認
        verify(exactly = 1) { todoService.saveAll(todoList = any<List<Todo>>()) }
    }
}