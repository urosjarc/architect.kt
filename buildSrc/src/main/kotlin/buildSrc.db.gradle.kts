plugins {
    kotlin("jvm")
}

dependencies {
    implementation("com.zaxxer:HikariCP:5.1.0")

    val exposedVersion = "0.49.0"
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")

    val postgresqlVersion = "42.7.3"
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    testImplementation("org.xerial:sqlite-jdbc:3.45.3.0")
}
