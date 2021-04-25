package com.example.todoList.controller

import com.example.todoList.TimestampConverter
import com.example.todoList.entity.Todo
import com.example.todoList.form.TodoListForm
import com.example.todoList.form.TodoUploadForm
import com.example.todoList.service.TodoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.text.SimpleDateFormat


@Controller
class DownloadController @Autowired constructor(private val todoService: TodoService) {
    @GetMapping("/download")
    fun download(): ResponseEntity<ByteArray> {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=utf-8''" + URLEncoder.encode("TODOダウンロード.csv", StandardCharsets.UTF_8.name()))
            .body(todoService.getDownloadCsvData().toByteArray(StandardCharsets.UTF_8));
    }
}