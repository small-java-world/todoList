package com.example.todoList.controller

import com.example.todoList.TimestampConverter
import com.example.todoList.entity.Todo
import com.example.todoList.form.TodoUploadForm
import com.example.todoList.service.TodoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.text.SimpleDateFormat


@Controller
class UploadController @Autowired constructor(private val todoService: TodoService) {
    @GetMapping("/uploadForm")
    fun inputForm(@ModelAttribute todoUploadForm: TodoUploadForm): ModelAndView =
        ModelAndView("/uploadForm")

    @PostMapping("/upload")
    fun upload(todoUploadForm: TodoUploadForm, model: Model): String? {
        val dateFormat = SimpleDateFormat(TimestampConverter.LIMIT_TIME_FORMAT)
        val todoList = ArrayList<Todo>()

        try {
            BufferedReader(InputStreamReader(todoUploadForm.uploadedFile!!.inputStream, StandardCharsets.UTF_8)).use {
                    bufferedReader ->
                var csvLine = bufferedReader.readLine()
                while(csvLine != null) {
                    val csvLineElement = csvLine.split(",")
                    val todo: Todo = Todo(null, csvLineElement[0], csvLineElement[1], Timestamp(dateFormat.parse(csvLineElement[2]).time))
                    todoList.add(todo)
                    csvLine = bufferedReader.readLine()
                }

                if(!todoService.save(todoList)) {
                    model.addAttribute("errorMessage", "アップロード失敗")
                    return "/uploadForm"
                }
                return "redirect:/top/list";
            }
        } catch (e: IOException) {
            model.addAttribute("errorMessage", "アップロード失敗:ファイルが読み込めません")
            throw RuntimeException("ファイルが読み込めません", e)
        }

        return "/inputForm"
    }
}