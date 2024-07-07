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

package com.yugyd.buildlogic.convention.test

import com.android.build.api.dsl.CommonExtension
import com.yugyd.buildlogic.convention.core.ANDROID_TEST_IMPLEMENTATION
import com.yugyd.buildlogic.convention.core.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun CommonExtension<*, *, *, *, *, *>.configureTest(target: Project) {
    defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    target.dependencies {
        add(ANDROID_TEST_IMPLEMENTATION, target.libs.findLibrary("androidx-test-runner").get())
    }
}
