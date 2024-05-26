plugins {
    kotlin("jvm")
}

dependencies {
	implementation("com.google.crypto.tink:tink:1.13.0")
	implementation("org.springframework.security:spring-security-core:6.2.3")
	runtimeOnly("org.bouncycastle:bcprov-jdk18on:1.76")
}
