package com.example.todoList.service

import com.example.todoList.entity.Todo
import com.example.todoList.repository.TodoRepository
import com.example.todoList.timestampToString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TodoService @Autowired constructor(private val todoRepository: TodoRepository) {
    fun findTodoList(): MutableIterable<Todo> {
        return todoRepository.findAll()
    }

    fun findTodoListByTitle(searchCondTitle: String?): MutableIterable<Todo> {
        return todoRepository.findByTitle("%$searchCondTitle%")
    }

    fun findById(id: Int): Optional<Todo> {
        return todoRepository.findById(id)
    }

    fun save(todo: Todo): Boolean {
        return try {
            todoRepository.save(todo)
            true;
        } catch (e: Exception) {
            false;
        }
    }

    fun saveAll(todoList: List<Todo>): Boolean {
        return try {
            todoRepository.saveAll(todoList)
            true;
        } catch (e: Exception) {
            false;
        }
    }

    fun getDownloadCsvData(): String {
        var csvList = todoRepository.findAll()

        var stringBuilder = StringBuilder()
        csvList.forEach() {
            stringBuilder.append("${it.title},${it.content},${timestampToString(it.limittime!!)}${System.lineSeparator()}")
        }
        return stringBuilder.toString()
    }

}
