plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "architect"
includeBuild("architect-annotations")
includeBuild("architect-gradle")
includeBuild("architect-lib")
