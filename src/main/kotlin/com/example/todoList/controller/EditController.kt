package com.example.todoList.controller

import com.example.todoList.entity.Todo
import com.example.todoList.service.TodoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.support.SessionStatus
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid
import javax.validation.constraints.NotBlank


@Controller
@SessionAttributes("todo")
class EditController @Autowired constructor(private val todoService: TodoService) {
    @ModelAttribute("todo")
    fun setUpLtodo(): Todo? {
        return Todo(null, null, null, null)
    }

//    @GetMapping("/inputForm")
//    fun inputForm(@ModelAttribute todo: Todo): ModelAndView =
//        ModelAndView("/inputForm").apply { addObject("todo",
//            todo) }

    @GetMapping("/inputForm")
    fun inputForm(@ModelAttribute todo: Todo, model: Model) :String{
        todo.id == null
        todo.title = null
        todo.content = null
        todo.limittime = null

        model.addAttribute("todo", todo)
        return "/inputForm"
    }

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
    fun regist(@ModelAttribute @Valid todo:Todo, bindingResult: BindingResult,
               model: Model, sessionStatus: SessionStatus
    ): String {
        if(bindingResult.hasErrors()){
            return "/inputForm";
        }

        return if(todoService.save(todo)) {
            //@SessionAttributes("todo")をクリアして/top/listにリダイレクト
            sessionStatus.setComplete();
            "redirect:/top/list";
        } else {
            model.addAttribute("errorMessage", if(todo.id == null) "登録失敗" else "更新失敗")
            "/inputForm";
        }
    }

    @GetMapping("/backToList")
    fun back(sessionStatus: SessionStatus): String {
        //@SessionAttributes("todo")をクリアして/top/listにリダイレクト
        sessionStatus.setComplete();
        return "redirect:/top/list";
    }
}