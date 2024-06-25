
plugins {
    `kotlin-dsl`
}
repositories {
    gradlePluginPortal()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.7.10")
    implementation("org.jetbrains.kotlinx:kover:0.6.1")
    implementation("org.jetbrains.dokka:org.jetbrains.dokka.gradle.plugin:1.9.20")
    implementation("com.adarshr:gradle-test-logger-plugin:4.0.0")
}
