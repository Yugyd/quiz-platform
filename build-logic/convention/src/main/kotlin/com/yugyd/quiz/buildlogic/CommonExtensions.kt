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

package com.yugyd.quiz.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun configureAndroid(commonExtension: CommonExtension<*, *, *, *>) {
    commonExtension.apply {
        compileSdk = COMPILE_SDK

        defaultConfig {
            minSdk = MIN_SDK

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            resourceConfigurations.addAll(setOf("en", "ru"))
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}

internal fun configureKotlin(commonExtension: CommonExtension<*, *, *, *>) {
    (commonExtension as ExtensionAware).extensions.configure<KotlinJvmOptions>(
        "kotlinOptions"
    ) {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

internal fun CommonExtension<*, *, *, *>.configureLint() = lint {
    disable += mutableSetOf("VectorPath")

    // Disable lintVital, lint run in CI
    checkReleaseBuilds = false

    ignoreTestSources = true

    warningsAsErrors = true
    abortOnError = true
}

internal fun configureBuildTypes(commonExtension: CommonExtension<*, *, *, *>) {
    commonExtension.apply {
        buildTypes {
            getByName("debug") {
                isDebuggable = true
                isMinifyEnabled = false
            }

            getByName("release") {
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
}

internal fun Project.configureCompose(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures.compose = true

        composeOptions.kotlinCompilerExtensionVersion = libs.findVersion(
            "kotlinCompilerExtensionVersion"
        ).get().toString()

        dependencies {
            val bom = libs.findLibrary("compose-bom").get()
            add(IMPLEMENTATION, platform(bom))

            // Enable preview
            add(DEBUG_IMPLEMENTATION, libs.findLibrary("compose-ui-tooling").get())
            add(IMPLEMENTATION, libs.findLibrary("compose-ui-tooling-preview").get())
        }
    }
}
