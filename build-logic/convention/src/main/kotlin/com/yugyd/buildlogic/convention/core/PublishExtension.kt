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

package com.yugyd.buildlogic.convention.core

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

internal fun Project.configureMavenInfo() {
    group = project.property("ARTIFACT_GROUP").toString()
    version = project.property("ARTIFACT_VERSION").toString()
}

internal fun Project.configureMavenRepositories() {
    extensions.configure<PublishingExtension> {
        repositories {
            maven {
                val localReleasesRepoUrl = layout.buildDirectory.dir("repos/releases")
                val localSnapshotsRepoUrl = layout.buildDirectory.dir("repos/snapshots")

                val releasesRepoUrl = getReleaseRepository()
                val snapshotsRepoUrl = getSnapshotRepository()

                val isSnapshotVersion = version.toString().endsWith("SNAPSHOT")

                url = if (
                    !snapshotsRepoUrl.isNullOrBlank() && !releasesRepoUrl.isNullOrBlank()
                ) {
                    credentials {
                        username = getRepositoryUsername()
                        password = getRepositoryPassword()
                    }

                    uri(
                        if (isSnapshotVersion) {
                            snapshotsRepoUrl
                        } else {
                            releasesRepoUrl
                        }
                    )
                } else {
                    uri(
                        if (isSnapshotVersion) {
                            localSnapshotsRepoUrl
                        } else {
                            localReleasesRepoUrl
                        }
                    )
                }
            }
        }
    }
}

private fun getRepositoryUsername(): String? = System.getenv("REPO_USERNAME")

private fun getRepositoryPassword(): String? = System.getenv("REPO_PASSWORD")

private fun getSnapshotRepository(): String? = System.getenv("REPO_SNAPSHOT_URL")

private fun getReleaseRepository(): String? = System.getenv("REPO_RELEASE_URL")

internal fun Project.configureMavenPublishing(packagingName: String, componentName: String) {
    extensions.configure<PublishingExtension> {
        publications {
            register<MavenPublication>("release") {
                artifactId = project.name

                pom {
                    packaging = packagingName

                    name.set(stringProperty("POM_NAME"))
                    description.set(stringProperty("POM_DESCRIPTION"))
                    url.set(stringProperty("POM_URL"))

                    licenses {
                        license {
                            name.set(stringProperty("POM_LICENSE_NAME"))
                            url.set(stringProperty("POM_LICENSE_URL"))
                            distribution.set(stringProperty("POM_LICENCE_DIST"))
                        }
                    }

                    developers {
                        developer {
                            id.set(stringProperty("POM_DEVELOPER_ID"))
                            name.set(stringProperty("POM_DEVELOPER_NAME"))
                            email.set(stringProperty("POM_DEVELOPER_EMAIL"))
                        }
                    }

                    scm {
                        connection.set(stringProperty("POM_SCM_CONNECTION"))
                        developerConnection.set(stringProperty("POM_SCM_DEVELOPER_CONNECTION"))
                        url.set(stringProperty("POM_SCM_URL"))
                    }
                }

                afterEvaluate {
                    from(components[componentName])
                }
            }
        }
    }
}
