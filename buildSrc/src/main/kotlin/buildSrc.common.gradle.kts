plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.adarshr.test-logger")
    id("org.jetbrains.kotlinx.kover")
}
kotlin {
    jvmToolchain(19)
}
repositories {
    mavenCentral()
}
testlogger {
    this.setTheme("mocha")
}

dokka {
    moduleName.set("Project Name")
    dokkaSourceSets.main {
        includes.from("README.md")
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl("https://example.com/src")
            remoteLineSuffix.set("#L")
        }
    }
    pluginsConfiguration.html {
        customStyleSheets.from("styles.css")
        customAssets.from("logo.png")
        footerMessage.set("(c) Your Company")
    }
}

//tasks.dokkaHtml {
//    dokkaSourceSets {
//        configureEach {
//            documentedVisibilities.set(
//                setOf(
//                    org.jetbrains.dokka.DokkaConfiguration.Visibility.PUBLIC, // Same for both Kotlin and Java
//                    org.jetbrains.dokka.DokkaConfiguration.Visibility.PRIVATE, // Same for both Kotlin and Java
//                    org.jetbrains.dokka.DokkaConfiguration.Visibility.PROTECTED, // Same for both Kotlin and Java
//                    org.jetbrains.dokka.DokkaConfiguration.Visibility.INTERNAL, // Kotlin-specific internal modifier
//                    org.jetbrains.dokka.DokkaConfiguration.Visibility.PACKAGE, // Java-specific package-private visibility
//                )
//            )
//            includeNonPublic.set(true)
//            jdkVersion.set(19)
//            reportUndocumented.set(true)
//            skipEmptyPackages.set(false)
//        }
//    }
//}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

group = "com.solvesall"
version = "0.0.0"

tasks.test {
    useJUnitPlatform()
}
