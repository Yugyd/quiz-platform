package com.yugyd.quiz.core.test

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun <T> StateFlow<T>.test(): MutableList<T> {
    val items = mutableListOf<T>()

    GlobalScope.launch(Dispatchers.Unconfined) {
        collect {
            items += it
        }
    }

    return items
}
