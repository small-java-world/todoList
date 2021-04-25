package com.example.todoList.service

import com.example.todoList.entity.Todo
import com.example.todoList.repository.TodoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

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

    fun save(todo:Todo) :Boolean {
        return try {
            todoRepository.save(todo)
            true;
        }catch (e:Exception) {
            false;
        }
    }

}
