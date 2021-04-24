package com.example.todoList

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions
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
class TopControllerTest : ControllerTestBase() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var todoService: TodoService

    @Test
    fun `default list`() {
        val mockResultTodoList = generateMockResultTotoDtoList(false)
            every { todoService.findTodoList() } returns mockResultTodoList

        val mvcResult = mockMvc.perform(get("/top/list"))
            .andExpect(status().isOk)
            .andReturn()

        //レスポンスを文字列で取り出してexpectedListResult.txtと比較する。
        //mvcResult.response.contentAsStringは途中で切られる事あるので無条件にはおすすめできないです。
        //長さが短いレスポンスのみ期待値ファイルと比較するとのやり方は、テストの実装効率、変更への強さの両面からおすすめです。
        assertFileEquals("expectedListResult.txt",
            mvcResult.response.contentAsString)

        verify(exactly = 1) { todoService.findTodoList() }

        val dateFormat = SimpleDateFormat("yyyy/MM/dd")

        //viewの確認は別の方法で実施する。との方針であればtodoListForm.todoListを取り出して検証で良いと思います。
        val todoListForm = mvcResult.modelAndView!!.modelMap["todoListForm"] as TodoListForm
        Assertions.assertThat(todoListForm.todoList).extracting("id", "title", "content", "limittimeLabel")
            .containsExactly(
                tuple(mockResultTodoList[0].id, mockResultTodoList[0].title, mockResultTodoList[0].content, dateFormat.format(mockResultTodoList[0].limittime)),
                tuple(mockResultTodoList[1].id, mockResultTodoList[1].title, mockResultTodoList[1].content, dateFormat.format(mockResultTodoList[1].limittime))
            )
    }

}