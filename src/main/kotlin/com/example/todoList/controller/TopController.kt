package com.example.todoList.controller

import com.example.todoList.form.TodoListForm
import com.example.todoList.service.TodoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/top")
class TopController @Autowired constructor(private val todoService: TodoService) {
    /**
     * /top/listにgetでアクセスするとlist.htmlが返却される。
     * modelAndViewのmodelMapに属性名:todoListFormでTodoListFormをセットします。
     * TodoListFormのtodoListにはtodoService.findTodoList()をセットしています。
     */
    @GetMapping("list")
    fun list(): ModelAndView =
        ModelAndView("/list").apply { addObject("todoListForm",
            TodoListForm(todoService.findTodoList(), null)
        ) }
}