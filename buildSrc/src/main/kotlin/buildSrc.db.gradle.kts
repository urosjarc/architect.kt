plugins {
	kotlin("jvm")
}

dependencies {
	implementation("com.zaxxer:HikariCP:5.1.0")

	val exposedVersion = "0.50.1"
	implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
	implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")

	runtimeOnly("org.postgresql:postgresql:42.7.1")
}
