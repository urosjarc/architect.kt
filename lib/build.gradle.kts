plugins {
    id("buildSrc.common")
    id("buildSrc.datetime")
    id("buildSrc.serialization")
    id("buildSrc.logging")
}

dependencies {
    implementation(project(":annotations"))
    implementation(kotlin("reflect"))
    implementation("io.github.classgraph:classgraph:4.8.172")
}
