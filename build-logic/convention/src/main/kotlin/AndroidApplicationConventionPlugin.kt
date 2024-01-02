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

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.yugyd.quiz.buildlogic.ANDROID_APPLICATION_PLUGIN_ID
import com.yugyd.quiz.buildlogic.KOTLIN_ANDROID_PLUGIN_ID
import com.yugyd.quiz.buildlogic.TARGET_SDK
import com.yugyd.quiz.buildlogic.configureAndroid
import com.yugyd.quiz.buildlogic.configureBuildTypes
import com.yugyd.quiz.buildlogic.configureKotlin
import com.yugyd.quiz.buildlogic.configureLint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    companion object {
        private val ACTIVE_PRODUCT_FLAVOR_VARIANTS = arrayOf("dev")
    }

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(ANDROID_APPLICATION_PLUGIN_ID)
            pluginManager.apply(KOTLIN_ANDROID_PLUGIN_ID)

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = TARGET_SDK

                configureAndroid(this)

                configureBuildTypes(this)

                configureKotlin(this)

                configureLint()

                disableUnusedProductFlavorVariants()
            }
        }
    }

    private fun Project.disableUnusedProductFlavorVariants() {
        // https://developer.android.com/build/build-variants#filter-variants
        extensions.configure<ApplicationAndroidComponentsExtension> {
            beforeVariants { variantBuilder ->
                if (!ACTIVE_PRODUCT_FLAVOR_VARIANTS.contains(variantBuilder.flavorName)) {
                    variantBuilder.enable = false
                }
            }
        }
    }
}
