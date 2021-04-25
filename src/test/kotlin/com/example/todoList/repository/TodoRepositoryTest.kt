package com.example.todoList.repository

import com.example.todoList.CsvDataSetLoader
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader::class)
@TestExecutionListeners(
    DependencyInjectionTestExecutionListener::class,
    DirtiesContextTestExecutionListener::class,
    TransactionDbUnitTestExecutionListener::class)
@Transactional
class TodoRepositoryTest @Autowired constructor(val todoRepository: TodoRepository){

    @Test
    @DatabaseSetup(value = ["/com/example/todoList/findByTitleBaseData"])
    fun findByTitle() {
        val todoResultList1 = todoRepository.findByTitle("%odo%")
        assertThat(todoResultList1).extracting("id", "title", "content")
            .containsExactly(
                tuple(1, "todo1", "todo1 content"),
                tuple(2, "todo2", "todo2 content"),
                tuple(3, "todo3", "todo3 content"),
                tuple(4, "todo4", "todo4 content"),
                tuple(5, "todo10", "todo10 content"))

        val todoResultList2 = todoRepository.findByTitle("%odo1%")
        assertThat(todoResultList2).extracting("id", "title", "content")
            .containsExactly(
                tuple(1, "todo1", "todo1 content"),
                tuple(5, "todo10", "todo10 content"))
    }

    @Test
    @DatabaseSetup(value = ["/com/example/todoList/findAllBaseData"])
    fun findAll() {
        val todoResultList1 = todoRepository.findAll()
        assertThat(todoResultList1).extracting("id", "title", "content")
            .containsExactly(
                tuple(100, "todo100", "todo100 content"),
                tuple(200, "todo200", "todo200 content"))
    }

}