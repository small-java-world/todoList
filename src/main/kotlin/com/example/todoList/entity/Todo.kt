package com.example.todoList.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Table(value = "todo")
    data class Todo(
    @Id
    @Column("id")
    var id: Int?,
    @field:NotBlank
    @field: Size(max = 10)
    @Column("title")
    var title: String?,
    @field: Size(max = 20)
    @Column("content")
    var content: String?,
    @Column("limittime")
    var limittime: Timestamp?
)