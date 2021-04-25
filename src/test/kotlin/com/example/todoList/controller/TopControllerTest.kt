package com.example.todoList.controller

import com.example.todoList.TestBase
import com.example.todoList.form.TodoListForm
import com.example.todoList.service.TodoService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.text.SimpleDateFormat


@ExtendWith(SpringExtension::class)
@WebMvcTest(TopController::class)
class TopControllerTest : TestBase() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    //todoServiceをMockkBeanとしてインジェクション
    //これでmockMvcが利用するTodoServiceがモック化されます。
    @MockkBean
    private lateinit var todoService: TodoService

    //TopController#listのテスト
    @Test
    fun `default list`() {
        //todoService.findTodoList()が返却さる結果を生成
        val mockResultTodoList = generateMockResultTotoList(false)

        //todoService.findTodoList()でmockResultTodoListが返却されるようにする。
        every { todoService.findTodoList() } returns mockResultTodoList

        // /top/listをgetし、結果ステータスがOKであることを検証し、
        // andReturn()でMvcResultを取得しておく。
        val mvcResult = mockMvc.perform(get("/top/list"))
            .andExpect(status().isOk)
            .andReturn()

        //レスポンスを文字列で取り出してexpectedListResult.txtと比較する。
        //mvcResult.response.contentAsStringは途中で切られる事あるので無条件にはおすすめできないですが、
        //長さが短いレスポンスのみ期待値ファイルと比較するとのやり方は、テストの実装効率、変更への強さの両面からおすすめです。
        assertFileEquals("expectedListResult.txt",
            mvcResult.response.contentAsString)

        //viewの中身の確認は別の方法で実施する。との方針であればmvcResultからtodoListForm.todoListを取り出して検証で良いと思います。
        val todoListForm = mvcResult.modelAndView!!.modelMap["todoListForm"] as TodoListForm

        //assertThat(todoListForm.todoList)のサイズを検証
        assertThat(todoListForm.todoList).hasSize(2)

        //todoListForm.todoListをAssertJのextractingで取り出し、containsExactlyのtuple指定で
        //要素が順序も含め完全に一致することを検証しています。
        assertThat(todoListForm.todoList).extracting("id", "title", "content", "limittime")
            .containsExactly(
                tuple(mockResultTodoList[0].id, mockResultTodoList[0].title, mockResultTodoList[0].content, mockResultTodoList[0].limittime),
                tuple(mockResultTodoList[1].id, mockResultTodoList[1].title, mockResultTodoList[1].content, mockResultTodoList[1].limittime)
            )

        //todoService.findTodoList()が呼び出された回数(1)を検証
        verify(exactly = 1) { todoService.findTodoList() }
    }
}