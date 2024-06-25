plugins {
    id("buildSrc.common")
    id("buildSrc.datetime")
    id("buildSrc.serialization")
    id("buildSrc.logging")
    id("com.github.johnrengelman.shadow")
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":annotations"))
    implementation(kotlin("reflect"))
    implementation("io.github.classgraph:classgraph:4.8.172")
}
