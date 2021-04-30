package com.example.todoList.controller

import com.example.todoList.entity.Todo
import com.example.todoList.service.TodoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.support.SessionStatus
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid


@Controller
class EditController @Autowired constructor(private val todoService: TodoService) {
    @GetMapping("/inputForm")
    fun inputForm(@ModelAttribute todo: Todo): ModelAndView =
        ModelAndView("/inputForm").apply { addObject("todo",
            todo) }

    @PostMapping("/save")
    fun save(@ModelAttribute @Valid todo:Todo, bindingResult: BindingResult,
               model: Model, sessionStatus: SessionStatus
    ): String {
        return if(todoService.save(todo)) {
            "redirect:/top/list";
        } else {
            model.addAttribute("errorMessage", "登録失敗")
            "/inputForm";
        }
    }
}