plugins {
    kotlin("jvm") version("1.9.23")
}
kotlin {
    explicitApi()
}
repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

group = "com.urosjarc"
version = "0.0.0"

tasks.test {
    useJUnitPlatform()
}
