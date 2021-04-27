package com.example.todoList.entity

import com.example.todoList.timestampToString
import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.format.annotation.DateTimeFormat
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
    @field: Size(max = 12)
    @Column("content")
    var content: String?,
    @Column("limittime")
    var limittime: Timestamp?
) {
    val limittimeText: String
        get() = if (limittime != null) timestampToString(limittime!!) else ""
}