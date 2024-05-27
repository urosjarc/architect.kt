plugins {
    id("buildSrc.common")
    id("buildSrc.serialization")
    id("buildSrc.db")
    id("buildSrc.datetime")
}
dependencies {
    implementation(project(":annotations"))
    implementation(project(":lib"))
    implementation(project(":core"))


}

kotlin {
    explicitApi()
}
