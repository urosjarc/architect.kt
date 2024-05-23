plugins {
    `jvm-test-suite`
    kotlin("jvm") version "1.9.21"
}

group = "com.urosjarc"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.classgraph:classgraph:4.8.172")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin {
    explicitApi()
    jvmToolchain(19)
}

testing {
    suites {
        configureEach {
            if (this is JvmTestSuite) {
                useJUnitJupiter()
                dependencies {
                    implementation(project())
                }
            }
        }

        register<JvmTestSuite>("e2e") {
            dependencies {
                implementation("io.github.classgraph:classgraph:4.8.172")
            }
        }
    }
}
