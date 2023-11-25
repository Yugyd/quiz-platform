plugins {
    id 'quiz.android.library'
<#if enableDi>
    id 'quiz.android.hilt'
</#if>
}

android {
    namespace '${packageName}'
}

dependencies {
    // Modules
    implementation project(':product:shared:domain-api')
    implementation project(':product:shared:domain-utils')
    <#if enableAnalytics>
    implementation project(":analytics:analytics-bl")
    </#if>

    // Kotlin
    implementation libs.kotlinx.coroutines.android
    <#if enableUnitTest>

    // Testing
    testImplementation libs.junit.core
    testImplementation libs.kotlin.test
    </#if>
}
