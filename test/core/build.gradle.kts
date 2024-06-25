plugins {
    id("buildSrc.common")
    id("buildSrc.serialization")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":annotations"))
    implementation(project(":lib"))
}
