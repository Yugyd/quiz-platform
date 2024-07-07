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

package com.yugyd.buildlogic.convention.buildtype

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.dsl.BuildType as InternalBuild

internal fun CommonExtension<*, *, *, *, *, *>.configureBuildTypes() {
    buildTypes {
        getByName("debug") {
            if (this is ApplicationExtension) {
                isShrinkResources = false
            }
            isDebuggable = true
            isMinifyEnabled = false
        }

        create("staging") {
            initWith(getByName("release"))
            isDebuggable = true
        }

        getByName("release") {
            if (this is ApplicationExtension) {
                isShrinkResources = true
            }
            isDebuggable = false
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            matchingFallbacks.add("debug")
        }
    }
}

internal var BuildType.isDebuggable: Boolean
    get() = (this as InternalBuild).isDebuggable
    set(value) {
        (this as InternalBuild).isDebuggable = value
    }
