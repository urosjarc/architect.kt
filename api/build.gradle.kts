plugins {
	id("buildSrc.common")
	id("buildSrc.serialization")
	id("buildSrc.db")
	id("buildSrc.datetime")
}
dependencies {
	this.implementation(this.project(":core"))
}
