plugins {
    id("buildSrc.common")
    id("buildSrc.serialization")
    id("buildSrc.datetime")
    id("buildSrc.logging")
    id("buildSrc.db")
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":annotations"))
    implementation(kotlin("reflect"))
    implementation("io.github.classgraph:classgraph:4.8.172")
}
