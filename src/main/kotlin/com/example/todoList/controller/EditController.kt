package com.example.todoList.controller

import com.example.todoList.entity.Todo
import com.example.todoList.service.TodoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.bind.support.SessionStatus
import javax.validation.Valid


@Controller
@SessionAttributes("todo")
class EditController @Autowired constructor(private val todoService: TodoService) {
    @ModelAttribute("todo")
    fun setupTodo(): Todo? {
        return Todo(null, null, null, null)
    }

    @GetMapping("/inputForm")
    fun inputForm(@ModelAttribute todo: Todo, model: Model): String {
        //更新画面->ブラウザの戻るボタン->新規登録で更新対象のtodoが表示されるのでクリア
        todo.id == null
        todo.title = null
        todo.content = null
        todo.limittime = null
        return "/inputForm"
    }

    @GetMapping("/editForm")
    fun editForm(@ModelAttribute todo: Todo, model: Model): String {
        if (todo.id != null) {
            //パラメータのidで検索
            val resultTodo = todoService.findById(todo.id!!)
            if (resultTodo != null) {
                //検索結果が存在する場合はmodelに"todo"の属性名でセット
                model.addAttribute("todo", resultTodo.get())
            }
        } else {
            //パラメータのidが飛んで来なかった場合はtodoの値をクリア、
            todo.id == null
            todo.title = null
            todo.content = null
            todo.limittime = null
        }
        return "/inputForm"
    }

    @PostMapping("/save")
    fun save(
        @ModelAttribute @Valid todo: Todo, bindingResult: BindingResult,
        model: Model, sessionStatus: SessionStatus
    ): String {
        if (bindingResult.hasErrors()) {
            return "/inputForm";
        }

        return if (todoService.save(todo)) {
            //@SessionAttributes("todo")をクリアして/top/listにリダイレクト
            sessionStatus.setComplete();
            "redirect:/top/list";
        } else {
            model.addAttribute("errorMessage", if (todo.id == null) "登録失敗" else "更新失敗")
            "/inputForm";
        }
    }

    @GetMapping("/backToList")
    fun backToList(sessionStatus: SessionStatus): String {
        //@SessionAttributes("todo")をクリアして/top/listにリダイレクト
        sessionStatus.setComplete();
        return "redirect:/top/list";
    }
}