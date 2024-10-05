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
    implementation("com.lemonappdev:konsist:0.16.1")
    implementation("io.github.classgraph:classgraph:4.8.172")
}
