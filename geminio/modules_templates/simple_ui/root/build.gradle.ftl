plugins {
    id 'quiz.android.library'
    id 'quiz.android.hilt'
    id 'quiz.android.compose.library'
}

android {
    namespace '${packageName}'
}

dependencies {
    // Module
    implementation project(':common-ui')
    implementation project(':uikit')
    implementation project(':navigation')
    implementation project(':core')
    implementation project(':${blModuleName}')
    implementation project(':domain-api')

    // UI - Compose
    implementation libs.compose.material3
    implementation libs.compose.viewmodel
    implementation libs.compose.lifecycle
    // Navigation
    implementation libs.compose.hilt.navigation

    // Play Services: Ads, Auth, Games
    implementation libs.play.ads
    <#if enableUnitTest>

    // Testing
    testImplementation libs.junit.core
    testImplementation libs.kotlin.test
    </#if>
}
