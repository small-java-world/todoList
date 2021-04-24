package com.example.todoList.dto

import java.sql.Timestamp

/**
 * Todoエンティティの画面用DTO　
 * limittimeを文字列でやり取りする必要があるのでlimittimeTextを含んでいます。
 */
data class TodoDto (
    var id: Long?,
    var title: String?,
    var content: String?,
    var limittime: Timestamp?,
    var limittimeText: String?
)