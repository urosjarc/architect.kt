plugins {
    id("buildSrc.common")
    id("buildSrc.datetime")
    id("buildSrc.serialization")
    id("buildSrc.db")
    id("buildSrc.logging")
    id("com.github.johnrengelman.shadow")
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":annotations"))
    implementation(project(":lib"))
    testImplementation(project(":lib"))
}
