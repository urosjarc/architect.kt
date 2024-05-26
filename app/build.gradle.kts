plugins {
    id("buildSrc.common")
    id("buildSrc.serialization")
    id("buildSrc.db")
    id("buildSrc.datetime")
}
dependencies {
    implementation(project(":public"))
    implementation(project(":core"))
    implementation(kotlin("reflect"))
    implementation("io.github.classgraph:classgraph:4.8.172")
}

kotlin {
    explicitApi()
}
