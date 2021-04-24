package com.example.todoList.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp

@Table(value = "todo")
data class Todo(
    @Id
    @Column("id")
    var id: Long?,
    @Column("title")
    var title: String,
    @Column("content")
    var content: String,
    @Column("limittime")
    var limittime: Timestamp
)