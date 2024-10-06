plugins {
    alias(libs.plugins.convention.library)
<#if enableDi>
    alias(libs.plugins.convention.hilt)
</#if>
}

android {
    namespace '${packageName}'
}

dependencies {
}
