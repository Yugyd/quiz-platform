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
    implementation project(':product:core:common-ui')
    implementation project(':product:designsystem:uikit')
    implementation project(':product:core:navigation')
    implementation project(':product:core')
    implementation project(':${blModuleName}')
    implementation project(':product:shared:domain-api')

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
