
plugins {
    `kotlin-dsl`
}
repositories {
    gradlePluginPortal()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.8.0")
    implementation("org.jetbrains.kotlinx:kover:0.6.1")
    implementation("org.jetbrains.dokka:org.jetbrains.dokka.gradle.plugin:2.0.0")
    implementation("com.adarshr:gradle-test-logger-plugin:4.0.0")
}
