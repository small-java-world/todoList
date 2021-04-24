package com.example.todoList.service

import com.example.todoList.dto.TodoDto
import com.example.todoList.entity.Todo
import com.example.todoList.repository.TodoRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat

@Service
class TodoService @Autowired constructor(private val todoRepository: TodoRepository) {
    companion object {
        const val LIMIT_TIME_FORMAT = "yyyy/MM/dd"
    }

    /**
     * todoRepository.findAll()の結果をList<TodoDto>に変換して返却
     */
    fun findTodoList(): List<TodoDto> {
        val todoList = todoRepository.findAll()
        return convertToTodoListResult(todoList)
    }

    /**
     * MutableIterable<Todo>をList<TodoDto>に変換して返却
     * といっても、todo.limittimeをyyyy/MM/ddでフォーマットしているだけです。
     */
    private fun convertToTodoListResult(todoList: MutableIterable<Todo>): List<TodoDto> {
        val result = ArrayList<TodoDto>()
        val dateFormat = SimpleDateFormat(LIMIT_TIME_FORMAT)
        for (todo in todoList) {
            val todoDto = TodoDto(todo.id, todo.title, todo.content, todo.limittime, dateFormat.format(todo.limittime))
            result.add(todoDto)
        }
        return result
    }

}
