package com.example.todoList

import com.example.todoList.entity.Todo
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.validation.ConstraintViolation

//errorMessageがnot nullの場合はvalidationResultにpropertyPath=titleでmessage=errorMessageの結果が含まれる確認
//errorMessageがnullの場合はvalidationResultにpropertyPath=titleの結果が含まれないこと確認
fun <T> includePropertyPathMessage(propertyPath: String, errorMessage:String?, validationResult: MutableSet<ConstraintViolation<T>>?): Boolean {
    if (validationResult != null) {
        return if(errorMessage == null) {
            !validationResult.stream().anyMatch { it -> it.propertyPath.toString() == propertyPath}
        } else {
            validationResult.stream().anyMatch { it -> it.propertyPath.toString() == propertyPath && it.message == errorMessage}
        }
    }
    return false;
}