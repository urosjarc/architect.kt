import com.urosjarc.architect.lib.Architect

plugins {
    id("buildSrc.common")
    id("buildSrc.serialization")
}

buildscript {
    dependencies {
        classpath(files("../core/build/libs/core-0.0.0-all.jar"))
        classpath(files("../../lib/build/libs/lib-0.0.0-all.jar"))
    }
}

task("domainSpace") {
    group = "Architect"
    doLast {
        val state = Architect.getState("com.urosjarc.architect.test")
        state.domainEntities.forEach {
            println(it)
        }
        println(state)
    }
}
