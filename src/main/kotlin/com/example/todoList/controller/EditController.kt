package com.example.todoList.controller

import com.example.todoList.dto.TodoDto
import com.example.todoList.service.TodoService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
class EditController @Autowired constructor(private val todoService: TodoService) {
    val logger: Logger = LoggerFactory.getLogger(EditController::class.java)

    @GetMapping("/inputForm")
    fun inputForm(@ModelAttribute todoDto: TodoDto): ModelAndView =
        ModelAndView("/inputForm").apply { addObject("todoDto",
            todoDto) }

    @PostMapping("/regist")
    fun regist(@ModelAttribute todoDto:TodoDto, model: Model): String {
        logger.info("regist todo.title={} todo.content={}", todoDto.title, todoDto.content)
        if(todoService.save(todoDto)) {
            return "redirect:/top/list";
        }
        else {
            model.addAttribute("errorMessage", "登録失敗")
//            model.addAttribute("todoDto", todoDto)
            return "/inputForm";
        }

    }
}