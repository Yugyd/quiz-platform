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

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.yugyd.quiz.buildlogic.ANDROID_LIBRARY_PLUGIN_ID
import com.yugyd.quiz.buildlogic.KOTLIN_ANDROID_PLUGIN_ID
import com.yugyd.quiz.buildlogic.TARGET_SDK
import com.yugyd.quiz.buildlogic.configureAndroid
import com.yugyd.quiz.buildlogic.configureBuildTypes
import com.yugyd.quiz.buildlogic.configureKotlin
import com.yugyd.quiz.buildlogic.configureLint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(ANDROID_LIBRARY_PLUGIN_ID)
            pluginManager.apply(KOTLIN_ANDROID_PLUGIN_ID)

            extensions.configure<LibraryExtension> {
                defaultConfig.targetSdk = TARGET_SDK

                configureAndroid(this)

                configureBuildTypes(this)

                configureKotlin(this)

                configureLint()

                disableUnnecessaryAndroidTests()
            }
        }
    }

    private fun Project.disableUnnecessaryAndroidTests() {
        // https://developer.android.com/build/build-variants#filter-variants
        extensions.configure<LibraryAndroidComponentsExtension> {
            beforeVariants { variantBuilder ->
                variantBuilder.enableAndroidTest = variantBuilder.enableAndroidTest
                        && project.projectDir.resolve("src/androidTest").exists()
            }
        }
    }
}