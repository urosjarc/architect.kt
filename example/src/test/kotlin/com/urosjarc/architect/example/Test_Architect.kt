package com.urosjarc.architect.generators

import com.urosjarc.architect.lib.Architect
import com.urosjarc.architect.lib.Utils
import com.urosjarc.architect.lib.serializers.UUIDSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import java.io.File
import kotlin.test.Test


class Test_Architect {
    val folder = File("/home/urosjarc/vcs/architect.kt/example/src/main/kotlin/com/urosjarc/architect/example/app")

    val domainModelsGenerator = DomainModelsGenerator(
        modelFolder = File(folder, "models"),
    )
    val jetbrainsExposedRepositoryGenerator = JetbrainsExposedRepositoryGenerator(
        interfaceFolder = File(folder, "interfaces"),
        sqlFolder = File(folder, "sql"),
        repoFolder = File(folder, "repos"),
        modelFolder = File(folder, "models"),
        mapping = listOf(
            "com.urosjarc.architect.example.types.Id" to JetbrainsExposedTypeMapping(
                { "reference(\"${it.aProp.name}\", ${it.aTypeParams[0].name}Sql.table)" },
                { "Id(row[table.${it.aProp.name}].value)" },
                { ".value" },
            ),
            "kotlin.collections.List" to JetbrainsExposedTypeMapping(
                { "blob(\"${it.aProp.name}\")" },
                { "Json.encodeToString(row[table.${it.aProp.name}].value)" },
                { "" },
            ),
            "kotlinx.datetime.Instant" to JetbrainsExposedTypeMapping(
                { "varchar(\"${it.aProp.name}\", 200)" },
                { "row[table.${it.aProp.name}]" },
                { "" },
            )
        ),
    )
    val plantUMLDependencySpaceGenerator = PlantUMLDependencySpaceGenerator(
        outputFile = File(folder, "DependencySpace.plantuml"),
    )

    val plantUMLDomainSpaceGenerator = PlantUMLDomainSpaceGenerator(
        outputFile = File(folder, "DomainSpace.plantuml"),
    )

    val rawDependencyObjectGenerator = RawDependencyObjectGenerator(
        appFile = File(folder, "App.kt"),
        startMark = "//START MARK",
        endMark = "//END MARK"
    )

    @Test
    fun `test all`() {
        folder.deleteRecursively()
        folder.mkdirs()

        val aStateData = Architect.getStateData(scannedPackage = "com.urosjarc.architect.example", classPackages = Utils.classPackages)
        val json = Json {
            prettyPrint = true
            serializersModule = SerializersModule {
                contextual(UUIDSerializer)
            }
        }
        val aStateDataFile = File(folder, "aStateData.json")
        aStateDataFile.createNewFile()
        aStateDataFile.writeText(json.encodeToString(aStateData))

        domainModelsGenerator.generate(aStateData = aStateData)

        val aStateData2 = Architect.getStateData(scannedPackage = "com.urosjarc.architect.example", classPackages = Utils.classPackages)
        val aStateData2File = File(folder, "aStateData2.json")
        aStateData2File.createNewFile()
        aStateData2File.writeText(json.encodeToString(aStateData))

        plantUMLDependencySpaceGenerator.generate(aStateData = aStateData2)
        plantUMLDomainSpaceGenerator.generate(aStateData = aStateData2)
//        rawDependencyObjectGenerator.generate(aStateData = aStateData2)
    }

}
