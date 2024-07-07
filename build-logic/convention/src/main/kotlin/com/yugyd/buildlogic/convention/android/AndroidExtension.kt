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

import com.android.build.api.dsl.CommonExtension
import com.yugyd.buildlogic.convention.core.JAVA_VERSION
import com.yugyd.buildlogic.convention.core.findVersionInt
import com.yugyd.buildlogic.convention.core.libs
import org.gradle.api.Project

internal fun CommonExtension<*, *, *, *, *, *>.configureAndroid(target: Project) {
    compileSdk = target.libs.findVersionInt("compile-sdk")

    defaultConfig {
        minSdk = target.libs.findVersionInt("min-sdk")
    }

    compileOptions {
        sourceCompatibility = JAVA_VERSION
        targetCompatibility = JAVA_VERSION
    }
}
