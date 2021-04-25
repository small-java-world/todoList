package com.example.todoList

import com.example.todoList.entity.Todo
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File
import java.net.URL
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.text.SimpleDateFormat

open class TestBase {
    fun assertFileEquals(expectedResultFileName: String, actual: String) {
        val expectedResult = readText(expectedResultFileName)
        assertEquals(expectedResult, actual)
    }

    private fun readText(fileName: String): String {
        val url: URL = this.javaClass.getResource(".")
        val fileFullPath = url.path + File.separator + fileName
        val targetFile = File(fileFullPath)
        if(targetFile.exists()) {
            return FileUtils.readFileToString(targetFile, StandardCharsets.UTF_8);
        }
        return "not exist fileName:$fileName"
    }

    fun generateMockResultTotoList(isSearch:Boolean): MutableList<Todo> {
        return if(isSearch) {
            mutableListOf(
                Todo(5, "todo5", "5content", toTimestamp("2021/04/20")),
                Todo(6, "todo6", "6content", toTimestamp("2021/04/21"))
            )
        }else {
            mutableListOf(
                Todo(1, "todo1", "1content", toTimestamp("2021/04/18")),
                Todo(2, "todo2", "2content", toTimestamp("2021/04/19"))
            )
        }
    }
}