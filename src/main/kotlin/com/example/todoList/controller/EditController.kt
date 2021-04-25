package com.example.todoList.controller

import com.example.todoList.entity.Todo
import com.example.todoList.service.TodoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.servlet.ModelAndView


@Controller
@SessionAttributes("todo")
class EditController @Autowired constructor(private val todoService: TodoService) {
    @ModelAttribute("todo")
    fun setUpLtodo(): Todo? {
        return Todo(null, null, null, null)
    }

    @GetMapping("/inputForm")
    fun inputForm(@ModelAttribute todo: Todo): ModelAndView =
        ModelAndView("/inputForm").apply { addObject("todo",
            todo) }

    @GetMapping("/editForm")
    fun editForm(@ModelAttribute todo: Todo, model: Model): String {
        if(todo.id != null) {
            val resultTodo = todoService.findById(todo.id!!)
            if(resultTodo != null) {
                model.addAttribute("todo", resultTodo.get())
            }
        }
        return "/inputForm"
    }

    @PostMapping("/regist")
    fun regist(@ModelAttribute todo:Todo, model: Model): String {
        if(todoService.save(todo)) {
            return "redirect:/top/list";
        }
        else {
            model.addAttribute("errorMessage", if(todo.id == null) "登録失敗" else "更新失敗")
            return "/inputForm";
        }
    }
}