/*
 *    Copyright 2023 Roman Likhachev
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.yugyd.quiz.core

import kotlin.math.roundToInt

private const val MAX_PERCENT = 100.0

fun percent(one: Int, two: Int) = ((one.toDouble() / two.toDouble()) * MAX_PERCENT).roundToInt()

fun percent(one: Int, two: Int, twoMultiplier: Int) =
    ((one.toDouble() / (two * twoMultiplier).toDouble()) * MAX_PERCENT).roundToInt()

@Suppress("MagicNumber", "ReturnCount")
fun calculatePercentage(first: Int, second: Int): Double {
    if (first == 0) return 0.0
    if (second == 0) return 0.0

    return (first.toDouble() / second.toDouble()) * MAX_PERCENT
}

@Suppress("MagicNumber", "ReturnCount")
fun calculatePercentageToRounded(first: Int, second: Int) =
    calculatePercentage(first, second).roundToInt()
