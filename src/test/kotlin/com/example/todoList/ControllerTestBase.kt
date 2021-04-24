package com.example.todoList

open class ControllerTestBase : TestBase() {
    fun generateMockResultTotoDtoList(isSearch:Boolean): List<TodoDto> {
        return if(isSearch) {
            mutableListOf(
                TodoDto(5L, "todo5", "5content", toTimestamp("2021/04/20"), "2021/04/20"),
                TodoDto(6L, "todo6", "6content", toTimestamp("2021/04/21"), "2021/04/21"))
        }else {
            mutableListOf(
                TodoDto(1L, "todo1", "1content", toTimestamp("2021/04/18"),"2021/04/18"),
                TodoDto(2L, "todo2", "2content", toTimestamp("2021/04/19"),"2021/04/19"))
        }
    }
}