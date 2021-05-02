package com.example.todoList.controller

import com.example.todoList.service.TodoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Controller
class DownloadController @Autowired constructor(private val todoService: TodoService) {
    @GetMapping("/download")
    fun download(): ResponseEntity<ByteArray> {
        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=utf-8''" + URLEncoder.encode("TODOダウンロード.csv", StandardCharsets.UTF_8.name())
            )
            .body(todoService.getDownloadCsvData().toByteArray(StandardCharsets.UTF_8));
    }
}