package com.yugyd.quiz.domain.tasks

import com.yugyd.quiz.domain.tasks.model.FilterModel
import com.yugyd.quiz.domain.tasks.model.FilterType

interface FilterInteractor {

    fun getFilters(): List<FilterType>

    suspend fun applyFilters(
        filters: List<FilterModel>,
        allTaskIds: List<Int>,
        errorsIds: List<Int>,
        favorites: List<Int>,
    ): List<Int>
}
