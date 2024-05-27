plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "architect"
include("annotations")
include("lib")
include("core")
include("app")
include("api")
