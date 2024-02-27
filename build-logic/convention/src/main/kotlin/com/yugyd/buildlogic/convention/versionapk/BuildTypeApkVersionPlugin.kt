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

package com.yugyd.buildlogic.convention.versionapk

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.ApplicationVariant
import com.android.build.api.variant.VariantOutput
import com.android.build.api.variant.VariantOutputConfiguration.OutputType
import com.android.build.gradle.AppPlugin
import com.yugyd.buildlogic.convention.versionapk.tasks.VersionCodeTask
import com.yugyd.buildlogic.convention.versionapk.tasks.VersionNameTask
import com.yugyd.buildlogic.convention.versionapk.tasks.VerifyManifestTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

class BuildTypeApkVersionPlugin : Plugin<Project> {

    companion object {
        private const val RELEASE_BUILD_TYPE_NAME = "release"

        private const val VERSION_CODE_TASK_NAME_PREFIX = "computeVersionCodeFor"
        private const val VERSION_CODE_FILE_NAME = "staticVersionCode.txt"

        private const val VERSION_NAME_TASK_NAME_PREFIX = "computeVersionNameFor"
        private const val VERSION_NAME_FILE_NAME = "staticVersionName.txt"

        private const val VERSION_VERIFY_VERSION_TASK_NAME_PREFIX = "verifyVersionFor"
    }

    override fun apply(target: Project) {
        with(target) {
            plugins.withType(AppPlugin::class.java) {
                extensions.configure<ApplicationAndroidComponentsExtension> {
                    val releaseBuildTypeSelector = selector().withBuildType(RELEASE_BUILD_TYPE_NAME)

                    onVariants(releaseBuildTypeSelector) { variant ->
                        val mainOutput = variant.outputs.single {
                            it.outputType == OutputType.SINGLE
                        }

                        val verifyVersionTask = configureVerifyTask(variant)

                        configureVersionCodeTask(
                            variant = variant,
                            verifyVersionTask = verifyVersionTask,
                            mainOutput = mainOutput,
                        )

                        configureVersionNameTask(
                            variant = variant,
                            verifyVersionTask = verifyVersionTask,
                            mainOutput = mainOutput,
                        )
                    }
                }
            }
        }
    }

    private fun Project.configureVerifyTask(
        variant: ApplicationVariant,
    ): TaskProvider<VerifyManifestTask> {
        val verifyVersionTaskName = VERSION_VERIFY_VERSION_TASK_NAME_PREFIX + variant.variantName

        val verifyVersionTask = tasks.register(
            verifyVersionTaskName,
            VerifyManifestTask::class.java,
        ) {
            val variantApkPath = variant.artifacts.get(SingleArtifact.APK)
            apkFolder.set(variantApkPath)

            val variantBuiltArtifactsLoader = variant.artifacts.getBuiltArtifactsLoader()
            buildArtefactsLoader.set(variantBuiltArtifactsLoader)
        }

        return verifyVersionTask
    }

    private fun Project.configureVersionCodeTask(
        variant: ApplicationVariant,
        verifyVersionTask: TaskProvider<VerifyManifestTask>,
        mainOutput: VariantOutput,
    ) {
        val versionCodeTaskName = VERSION_CODE_TASK_NAME_PREFIX + variant.variantName
        val versionCodeTask = tasks.register(
            versionCodeTaskName,
            VersionCodeTask::class.java,
        ) {
            val outputFilePath = layout.buildDirectory.file(VERSION_CODE_FILE_NAME)
            outputFile.set(outputFilePath)
        }
        versionCodeTask.configure {
            setFinalizedBy(listOf(verifyVersionTask))
        }

        val versionCodeValue = versionCodeTask.flatMap { task ->
            task.outputFile.map { versionFile ->
                versionFile.asFile.readText().toInt()
            }
        }
        mainOutput.versionCode.set(versionCodeValue)
    }

    private fun Project.configureVersionNameTask(
        variant: ApplicationVariant,
        verifyVersionTask: TaskProvider<VerifyManifestTask>,
        mainOutput: VariantOutput,
    ) {
        val versionNameTaskName = VERSION_NAME_TASK_NAME_PREFIX + variant.variantName
        val versionNameTask = tasks.register(
            versionNameTaskName,
            VersionNameTask::class.java,
        ) {
            val outputFilePath = layout.buildDirectory.file(VERSION_NAME_FILE_NAME)
            outputFile.set(outputFilePath)
        }
        versionNameTask.configure {
            setFinalizedBy(listOf(verifyVersionTask))
        }

        val versionNameValue = versionNameTask.flatMap { task ->
            task.outputFile.map { versionFile ->
                versionFile.asFile.readText()
            }
        }
        mainOutput.versionName.set(versionNameValue)
    }

    private val ApplicationVariant.variantName get() = name.uppercaseFirstChar()
}
