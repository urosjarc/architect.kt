plugins {
    id("buildSrc.common")
    id("buildSrc.datetime")
    id("buildSrc.serialization")
    id("buildSrc.db")
    id("buildSrc.logging")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(project(":annotations"))
    testImplementation(project(":lib"))
    testImplementation(project(":generators"))
}
