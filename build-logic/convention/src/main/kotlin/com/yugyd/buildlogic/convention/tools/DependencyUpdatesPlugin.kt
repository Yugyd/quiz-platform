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
package com.yugyd.buildlogic.convention.tools

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.yugyd.buildlogic.convention.core.findPluginId
import com.yugyd.buildlogic.convention.core.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class DependencyUpdatesPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply(target.libs.findPluginId("update-versions"))

        target.tasks.withType(DependencyUpdatesTask::class.java) {
            rejectVersionIf {
                isNonStable(candidate.version) && !isNonStable(currentVersion)
            }
        }
    }

    private fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any {
            version.uppercase().contains(it)
        }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }
}
