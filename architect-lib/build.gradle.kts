
plugins {
    kotlin("jvm") version ("1.9.23")
    kotlin("plugin.serialization") version ("1.7.10")
}
kotlin {
    explicitApi()
}

kotlin {
    jvmToolchain(21)
    explicitApi()
}
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.urosjarc:architect-annotations")
    implementation(kotlin("reflect"))
    implementation("io.github.classgraph:classgraph:4.8.172")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.4.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

group = "com.urosjarc"
version = "0.0.0"

tasks.test {
    useJUnitPlatform()
}
