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
}
