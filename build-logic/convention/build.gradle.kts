import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.yugyd.quiz.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.update.versions.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "quiz.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "quiz.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("composeAndroidApplication") {
            id = "quiz.android.compose.application"
            implementationClass = "ComposeAndroidApplicationConventionPlugin"
        }
        register("composeAndroidLibrary") {
            id = "quiz.android.compose.library"
            implementationClass = "ComposeAndroidLibraryConventionPlugin"
        }
        register("hiltAndroid") {
            id = "quiz.android.hilt"
            implementationClass = "HiltAndroidConventionPlugin"
        }
        register("dependencyUpdates") {
            id = "quiz.dependency.updates"
            implementationClass = "DependencyUpdatesPlugin"
        }
    }
}
