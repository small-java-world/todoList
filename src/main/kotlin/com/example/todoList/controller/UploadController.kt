package com.example.todoList.controller

import com.example.todoList.entity.Todo
import com.example.todoList.form.TodoUploadForm
import com.example.todoList.service.TodoService
import com.example.todoList.toTimestamp
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


@Controller
class UploadController @Autowired constructor(private val todoService: TodoService) {
    @GetMapping("/uploadForm")
    fun inputForm(@ModelAttribute todoUploadForm: TodoUploadForm): ModelAndView =
        ModelAndView("/uploadForm")

    @PostMapping("/upload")
    fun upload(todoUploadForm: TodoUploadForm, model: Model): String? {
        val todoList = ArrayList<Todo>()

        try {
            BufferedReader(InputStreamReader(todoUploadForm.uploadedFile!!.inputStream, StandardCharsets.UTF_8)).use {
                    bufferedReader ->
                var csvLine = bufferedReader.readLine()
                while(csvLine != null) {
                    val csvLineElement = csvLine.split(",")
                    val todo: Todo = Todo(null, csvLineElement[0], csvLineElement[1], toTimestamp(csvLineElement[2]))
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

        return "/uploadForm"
    }
}