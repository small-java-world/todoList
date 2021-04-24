package com.example.todoList

import com.github.springtestdbunit.dataset.AbstractDataSetLoader
import org.dbunit.dataset.IDataSet
import org.dbunit.dataset.csv.CsvDataSet
import org.springframework.core.io.Resource

class CsvDataSetLoader : AbstractDataSetLoader() {
    override fun createDataSet(resource: Resource?): IDataSet {
        if (resource != null) {
            return CsvDataSet(resource.file)
        }
        return CsvDataSet(null)
    }
}