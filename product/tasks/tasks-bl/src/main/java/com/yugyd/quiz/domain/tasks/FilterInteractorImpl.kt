package com.yugyd.quiz.domain.tasks

import com.yugyd.quiz.core.coroutinesutils.DispatchersProvider
import com.yugyd.quiz.domain.tasks.model.FilterModel
import com.yugyd.quiz.domain.tasks.model.FilterType
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FilterInteractorImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
) : FilterInteractor {

    override fun getFilters(): List<FilterType> {
        return FilterType.entries.toList()
    }

    override suspend fun applyFilters(
        filters: List<FilterModel>,
        allTaskIds: List<Int>,
        errorsIds: List<Int>,
        favorites: List<Int>
    ): List<Int> = withContext(dispatchersProvider.default) {
        if (filters.none(FilterModel::isChecked)) {
            allTaskIds
        } else {
            var resultTasks = allTaskIds
            filters
                .filter(FilterModel::isChecked)
                .map(FilterModel::id)
                .forEach { filterType ->
                    resultTasks = applyFilter(
                        filterType = filterType,
                        taskIds = resultTasks,
                        errorsIds = errorsIds,
                        favorites = favorites,
                    )
                }

            resultTasks
        }
    }

    private fun applyFilter(
        filterType: FilterType,
        taskIds: List<Int>,
        errorsIds: List<Int>,
        favorites: List<Int>
    ): List<Int> {
        return when (filterType) {
            FilterType.ERRORS -> taskIds.filter(errorsIds::contains)
            FilterType.FAVORITES -> taskIds.filter(favorites::contains)
        }
    }
}
