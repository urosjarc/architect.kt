import com.urosjarc.architect.lib.Architect
import com.urosjarc.architect.lib.generators.*

plugins {
    id("buildSrc.common")
    id("buildSrc.serialization")
    id("buildSrc.db")
}

buildscript {
    dependencies {
        classpath(files("../core/build/libs/core-0.0.0-all.jar"))
        classpath(files("../../lib/build/libs/lib-0.0.0-all.jar"))
    }
}

dependencies {
    implementation(project(":test:core"))
}

task("generate") {
    group = "Architect"
    doLast {
        val domainSpaceFile = project.layout.buildDirectory.file("domain-space.plantuml").get().asFile
        val dependencySpaceFile = project.layout.buildDirectory.file("dependency-space.plantuml").get().asFile
        val interfaceFolder = project.layout.projectDirectory.file("../core/src/main/kotlin/com/urosjarc/architect/test/core/repos").asFile
        val modelFolder = project.layout.projectDirectory.file("../core/src/main/kotlin/com/urosjarc/architect/test/core/models").asFile
        val sqlFolder = project.layout.projectDirectory.file("src/main/kotlin/com/urosjarc/architect/test/app/repos/sql").asFile
        val repoFolder = project.layout.projectDirectory.file("src/main/kotlin/com/urosjarc/architect/test/app/repos/sql").asFile

        mkdir(project.layout.buildDirectory)
        mkdir(interfaceFolder)
        mkdir(modelFolder)
        mkdir(sqlFolder)
        mkdir(repoFolder)

        val aStateData = Architect.getStateData("com.urosjarc.architect.test.core")
        PlantUMLDomainSpaceGenerator(outputFile = domainSpaceFile).generate(aStateData = aStateData)
        PlantUMLDependencySpaceGenerator(outputFile = dependencySpaceFile).generate(aStateData = aStateData)
        DomainModelsGenerator(modelFolder = modelFolder).generate(aStateData = aStateData)
        RawDependencyObjectGenerator(
            appFile = project.layout.projectDirectory.file("src/main/kotlin/com/urosjarc/architect/test/app/App.kt").asFile,
            startMark = "//START",
            endMark = "//END"
        ).generate(aStateData = aStateData)
        JetbrainsExposedRepositoryGenerator(
            interfaceFolder = interfaceFolder,
            sqlFolder = sqlFolder,
            repoFolder = repoFolder,
            modelFolder = modelFolder,
            mapping = listOf(
                "com.urosjarc.architect.test.core.types.Id" to Triple(
                    { "reference(\"${it.aProp.name}\", ${it.aTypeParams[0].name}Sql.table)" },
                    { "Id(row[table.${it.aProp.name}].value)" },
                    { ".value" },
                ),
                "com.urosjarc.architect.test.core.domain.User.Type" to Triple(
                    { "varchar(\"${it.aProp.name}\", 200)" },
                    { "User.Type.valueOf(row[table.${it.aProp.name}])" },
                    { ".name" },
                ),
                "kotlinx.datetime.Instant" to Triple(
                    { "timestamp(\"${it.aProp.name}\")" },
                    { "row[table.${it.aProp.name}]" },
                    { "" },
                )
            ),
        ).generate(aStateData = aStateData)
    }
}
