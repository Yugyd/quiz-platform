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

package com.yugyd.buildlogic.convention.jacoco

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.yugyd.buildlogic.convention.core.libs
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.testing.Test
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Locale

internal fun Project.configureJacoco(
    androidComponentsExtension: AndroidComponentsExtension<*, *, *>,
) {
    extensions.configure(JacocoPluginExtension::class.java) {
        toolVersion = libs.findVersion("jacoco").get().toString()
    }

    val jacocoTestReport = tasks.create("jacocoTestReport")

    androidComponentsExtension.onVariants { variant ->
        val testTaskName = "test${variant.name.capitalize()}UnitTest"

        val jacocoReportTaskName = "jacoco${testTaskName.capitalize()}Report"

        val reportTask = createJacocoReportTask(
            variant = variant,
            jacocoReportTaskName = jacocoReportTaskName,
            testTaskName = testTaskName,
        )

        jacocoTestReport.dependsOn(reportTask)
    }

    tasks.withType(Test::class.java).configureEach {
        extensions.configure(JacocoTaskExtension::class.java) {
            excludes = listOf("jdk.internal.*")
        }
    }
}

private fun Project.createJacocoReportTask(
    variant: Variant,
    jacocoReportTaskName: String,
    testTaskName: String,
): TaskProvider<JacocoReport> {
    val reportTask = tasks.register(jacocoReportTaskName, JacocoReport::class.java) {
        dependsOn(testTaskName)

        reports {
            html.required.set(true)
            xml.required.set(false)
        }

        val buildDir = layout.buildDirectory.get().asFile

        val tmpClassDirectoriesFileTree = fileTree(
            "$buildDir/tmp/kotlin-classes/${variant.name}"
        ) {
            exclude(coverageExclusions)
        }
        classDirectories.setFrom(tmpClassDirectoriesFileTree)

        val defaultSrcFiles = files(
            "$projectDir/src/main/java",
            "$projectDir/src/main/kotlin",
        )
        sourceDirectories.setFrom(defaultSrcFiles)

        val executionFile = file("$buildDir/jacoco/$testTaskName.exec")
        executionData.setFrom(executionFile)
    }

    return reportTask
}

private val coverageExclusions = listOf(
    // Android
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*"
)

private fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}
