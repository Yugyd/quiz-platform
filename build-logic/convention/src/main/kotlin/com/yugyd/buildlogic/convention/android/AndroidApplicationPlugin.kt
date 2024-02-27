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

package com.yugyd.buildlogic.convention.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.AbstractAppExtension
import com.yugyd.buildlogic.convention.core.ANDROID_APPLICATION_ALIAS
import com.yugyd.buildlogic.convention.core.ANDROID_CACHE_FIX_ALIAS
import com.yugyd.buildlogic.convention.core.KOTLIN_ANDROID_ALIAS
import com.yugyd.buildlogic.convention.core.configureKotlin
import com.yugyd.buildlogic.convention.core.findPluginId
import com.yugyd.buildlogic.convention.core.findVersionInt
import com.yugyd.buildlogic.convention.core.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.findPluginId(ANDROID_APPLICATION_ALIAS))
            pluginManager.apply(libs.findPluginId(KOTLIN_ANDROID_ALIAS))
            pluginManager.apply(libs.findPluginId(ANDROID_CACHE_FIX_ALIAS))

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = libs.findVersionInt("target-sdk")

                configureAndroid(target)

                configureKotlin()

                disableResInDebug()
            }
        }
    }

    private fun Project.disableResInDebug() {
        extensions.configure<AbstractAppExtension> {
            applicationVariants.all {
                if (buildType.name == "debug") {
                    mergedFlavor.resourceConfigurations.clear()
                    mergedFlavor.resourceConfigurations.addAll(arrayOf("xxhdpi", "en"))
                }
            }
        }
    }
}
