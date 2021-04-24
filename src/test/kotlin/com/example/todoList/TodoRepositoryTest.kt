package com.example.todoList

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
    @DatabaseSetup(value = ["/com/example/todoList/findAllBaseData"])
    fun findAll() {
        val todoResultList1 = todoRepository.findAll()
        assertThat(todoResultList1).extracting("id", "title", "content")
            .containsExactly(
                tuple(100L, "todo100", "todo100 content"),
                tuple(200L, "todo200", "todo200 content"))
    }
}