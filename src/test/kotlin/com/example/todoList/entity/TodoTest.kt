package com.example.todoList.entity

import com.example.todoList.includePropertyPathMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.context.SpringBootTest
import java.util.stream.Stream
import javax.validation.Validation
import javax.validation.Validator


@SpringBootTest
class TodoTest {
    companion object {
        private var validator: Validator? = null

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            validator = Validation.buildDefaultValidatorFactory().validator
        }

        @JvmStatic
        fun validateTodoTitleSource(): Stream<Arguments>? {
            val blankErrorMessage = "空白は許可されていません"
            return Stream.of(
                Arguments.of("", blankErrorMessage),
                Arguments.of(" ", blankErrorMessage),
                Arguments.of("12345678901", "0 から 10 の間のサイズにしてください"),
                Arguments.of("1234567890", null),
                Arguments.of("title", null));
        }

        @JvmStatic
        fun validateTodoContentSource(): Stream<Arguments>? {
            return Stream.of(
                Arguments.of("1234567890123", "0 から 12 の間のサイズにしてください"),
                Arguments.of("123456789012", null),
                Arguments.of("", null),
                Arguments.of(" ", null));
        }
    }

    @ParameterizedTest
    @MethodSource("validateTodoTitleSource")
    fun `validate todo title`(inputTitle:String, errorMessage:String?) {
        val todo = Todo(null, inputTitle, "", null)
        var violationResult = validator?.validate(todo)

        //errorMessageがnullはバリデーションチェックにひっかからないのでsize=0、それ以外はsize=1
        assertEquals(if(errorMessage != null) 1 else 0, violationResult!!.size)
        assertTrue(includePropertyPathMessage("title", errorMessage , violationResult))
    }

    @ParameterizedTest
    @MethodSource("validateTodoContentSource")
    fun `validate todo content`(inputContent:String, errorMessage:String?) {
        val todo = Todo(null, "title", inputContent, null)
        var violationResult = validator?.validate(todo)

        //errorMessageがnullはバリデーションチェックにひっかからないのでsize=0、それ以外はsize=1
        assertEquals(if(errorMessage != null) 1 else 0, violationResult!!.size)
        assertTrue(includePropertyPathMessage("content", errorMessage , violationResult))
    }


}