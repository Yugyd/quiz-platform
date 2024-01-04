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

package com.yugyd.quiz.navigation

import androidx.navigation.NamedNavArgument

fun RouteModel.getRouteWithArguments(
    argumentBuilder: (String) -> String?,
): String {
    return buildString {
        append(routePath)

        if (arguments.isNotEmpty()) {
            append("/")

            arguments.forEachIndexed { index, argName ->
                val argument = argumentBuilder(argName.name)
                append(argument)

                if (index < arguments.lastIndex) {
                    append("&")
                }
            }
        }
    }
}

fun calculateRouteModel(
    route: String,
    arguments: List<NamedNavArgument> = emptyList()
): RouteModel {
    val routeWithArgs = buildString {
        append(route)

        if (arguments.isNotEmpty()) {
            append("/")

            arguments.forEachIndexed { index, argName ->
                append("{")
                append(argName.name)
                append("}")

                if (index < arguments.lastIndex) {
                    append("&")
                }
            }
        }
    }

    return RouteModel(
        routePath = route,
        route = routeWithArgs,
        arguments = arguments,
    )
}
