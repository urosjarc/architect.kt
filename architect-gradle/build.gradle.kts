plugins {
    kotlin("jvm") version("1.9.23")
    `java-gradle-plugin`
}
repositories {
    gradlePluginPortal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("architectPlugin") {
            id = "com.urosjarc.architect"
            implementationClass = "com.urosjarc.architect.ArchitectPlugin"
        }
    }
}

dependencies {
    implementation("com.urosjarc:architect-lib")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.7.10")
    implementation("org.jetbrains.kotlinx:kover:0.6.1")

    implementation("org.jetbrains.dokka:org.jetbrains.dokka.gradle.plugin:1.9.20")
    implementation("com.adarshr:gradle-test-logger-plugin:4.0.0")
    implementation("io.github.classgraph:classgraph:4.8.172")
}
