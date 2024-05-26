plugins {
    id("buildSrc.common")
    id("buildSrc.datetime")
    id("buildSrc.serialization")
    id("buildSrc.security")
}

dependencies {
    implementation(project(":public"))
}

kotlin {
    explicitApi()
}
