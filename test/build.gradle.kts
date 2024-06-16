plugins {
    id("buildSrc.common")
    id("buildSrc.serialization")
    id("buildSrc.datetime")
    id("buildSrc.logging")
    id("buildSrc.archGen")
}

dependencies {
    implementation("io.github.classgraph:classgraph:4.8.172")
}
