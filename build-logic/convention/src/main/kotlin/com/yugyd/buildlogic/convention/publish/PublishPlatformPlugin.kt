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

package com.yugyd.buildlogic.convention.publish

import com.yugyd.buildlogic.convention.core.JAVA_PLATFORM_ID
import com.yugyd.buildlogic.convention.core.MAVEN_PUBLISH_ID
import com.yugyd.buildlogic.convention.core.configureMavenInfo
import com.yugyd.buildlogic.convention.core.configureMavenRepositories
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get

class PublishPlatformPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(JAVA_PLATFORM_ID)
            pluginManager.apply(MAVEN_PUBLISH_ID)

            configureMavenInfo()

            configureMavenRepositories()

            extensions.configure<PublishingExtension> {
                publications {
                    register("bom", MavenPublication::class.java) {
                        artifactId = project.name

                        afterEvaluate {
                            from(target.components["javaPlatform"])
                        }
                    }
                }
            }
        }
    }
}
