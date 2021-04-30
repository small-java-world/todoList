package com.example.todoList.controller

import com.example.todoList.entity.Todo
import com.example.todoList.service.TodoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class EditController @Autowired constructor(private val todoService: TodoService) {
    @GetMapping("/inputForm")
    fun inputForm(@ModelAttribute todo: Todo): ModelAndView =
        ModelAndView("/inputForm").apply {
            addObject(
                "todo",
                todo
            )
        }

    @PostMapping("/save")
    fun save(
        @ModelAttribute todo: Todo,
        model: Model
    ): String {
        return if (todoService.save(todo)) {
            "redirect:/top/list";
        } else {
            model.addAttribute("errorMessage", "登録失敗")
            "/inputForm";
        }
    }
}