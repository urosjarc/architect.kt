package com.urosjarc.architect.lib

import com.urosjarc.architect.lib.generators.DomainModelsGenerator
import com.urosjarc.architect.lib.generators.JetbrainsExposedRepositoryGenerator
import com.urosjarc.architect.lib.generators.JetbrainsExposedTypeMapping
import com.urosjarc.architect.lib.generators.PlantUMLDependencySpaceGenerator
import com.urosjarc.architect.lib.generators.PlantUMLDomainSpaceGenerator
import com.urosjarc.architect.lib.generators.RawDependencyObjectGenerator
import com.urosjarc.architect.lib.serializers.UUIDSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import java.io.File
import kotlin.test.Test


class Test_Architect {
    val folder = File("/home/urosjarc/vcs/architect.kt/lib/src/main/kotlin/com/urosjarc/architect/generators/output")

    val domainModelsGenerator = DomainModelsGenerator(
        modelFolder = File(folder, "models"),
    )
    val jetbrainsExposedRepositoryGenerator = JetbrainsExposedRepositoryGenerator(
        interfaceFolder = File(folder, "interfaces"),
        sqlFolder = File(folder, "sql"),
        repoFolder = File(folder, "repos"),
        modelFolder = File(folder, "models"),
        mapping = listOf(
            "com.urosjarc.architect.lib.types.Id" to JetbrainsExposedTypeMapping(
                { "reference(\"${it.aProp.name}\", ${it.aTypeParams[0].name}Sql.table)" },
                { "Id(row[table.${it.aProp.name}].value)" },
                { ".value" },
            ),
            "kotlin.collections.List" to JetbrainsExposedTypeMapping(
                { "blob(\"${it.aProp.name}\")" },
                { "Json.encodeToString(row[table.${it.aProp.name}].value)" },
                { "" },
            ),
            "com.urosjarc.architect.lib.domain.AVisibility" to JetbrainsExposedTypeMapping(
                { "enumerationByName<AVisibility>(\"${it.aProp.name}\", 200)" },
                { "row[table.${it.aProp.name}]" },
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

        val aStateData = Architect.getStateData(classPackages = Utils.classPackages)
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

        val aStateData2 = Architect.getStateData(classPackages = Utils.classPackages)
        val aStateData2File = File(folder, "aStateData2.json")
        aStateData2File.createNewFile()
        aStateData2File.writeText(json.encodeToString(aStateData))

        plantUMLDependencySpaceGenerator.generate(aStateData = aStateData2)
        plantUMLDomainSpaceGenerator.generate(aStateData = aStateData2)
//        rawDependencyObjectGenerator.generate(aStateData = aStateData2)
    }

}
