/*
 *    Copyright 2024 Roman Likhachev
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

package com.yugyd.buildlogic.convention.core

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

internal val Project.libs
    get(): VersionCatalog = extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("libs")

internal fun Project.stringProperty(propertyName: String) =
    project.property(propertyName).toString()

internal fun VersionCatalog.findPluginId(alias: String): String =
    findPlugin(alias).get().get().pluginId

internal fun VersionCatalog.findVersionInt(alias: String) =
    findVersion(alias).get().toString().toInt()

internal fun Project.checkPlugin(alias: String) {
    val pluginId = libs.findPluginId(alias)
    check(
        pluginManager.hasPlugin(pluginId)
    ) {
        "$pluginId not found"
    }
}
