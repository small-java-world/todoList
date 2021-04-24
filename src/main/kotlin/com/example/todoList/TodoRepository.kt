package com.example.todoList

import org.springframework.data.repository.CrudRepository


interface TodoRepository : CrudRepository<Todo, Int> {
}