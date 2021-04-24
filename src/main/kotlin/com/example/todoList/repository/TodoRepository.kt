package com.example.todoList.repository

import com.example.todoList.entity.Todo
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository


interface TodoRepository : CrudRepository<Todo, Int> {
    @Query("SELECT * FROM todo WHERE title LIKE :title")
    fun findByTitle(title: String): MutableIterable<Todo>
}
