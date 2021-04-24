package com.example.todoList.repository

import com.example.todoList.entity.Todo
import org.springframework.data.repository.CrudRepository


interface TodoRepository : CrudRepository<Todo, Int> {
}