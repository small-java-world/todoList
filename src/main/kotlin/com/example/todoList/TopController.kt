package com.example.todoList

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/top")
class TopController @Autowired constructor(private val todoService: TodoService) {
    @GetMapping("list")
    fun list(): ModelAndView =
            ModelAndView("/list").apply { addObject("todoListForm",
                    TodoListForm(todoService.findTodoList())) }
}