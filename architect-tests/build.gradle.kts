
plugins {
    kotlin("jvm") version ("1.9.23")
    kotlin("plugin.serialization") version ("1.7.10")
    id("com.urosjarc.architect") version ("0.0.0")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.urosjarc:architect-annotations:0.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

group = "com.urosjarc"
version = "0.0.0"

tasks.test {
    useJUnitPlatform()
}
