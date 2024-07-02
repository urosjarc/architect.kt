plugins {
    id("buildSrc.common")
    id("buildSrc.serialization")
    id("buildSrc.datetime")
    id("buildSrc.security")
    id("com.github.johnrengelman.shadow")
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":annotations"))
    implementation(project(":lib"))
}
