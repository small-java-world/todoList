package com.example.todoList

import javax.validation.ConstraintViolation

//expectedErrorMessageがnot nullの場合は、validationResultにpropertyPath=expectedPropertyPathでmessage=expectedErrorMessageの結果が含まれる場合true
//expectedErrorMessageがnullの場合は、validationResultにpropertyPath=expectedPropertyPathの結果が含まれない場合true
//それ以外はfalseを返却します。
fun <T> includePropertyPathMessage(
    expectedPropertyPath: String,
    expectedErrorMessage: String?,
    validationResult: MutableSet<ConstraintViolation<T>>?
): Boolean {
    if (validationResult != null) {
        return if (expectedErrorMessage == null) {
            !validationResult.stream().anyMatch { it -> it.propertyPath.toString() == expectedPropertyPath }
        } else {
            validationResult.stream()
                .anyMatch { it -> it.propertyPath.toString() == expectedPropertyPath && it.message == expectedErrorMessage }
        }
    }
    return false;
}