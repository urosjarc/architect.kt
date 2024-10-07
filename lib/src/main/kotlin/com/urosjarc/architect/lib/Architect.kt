package com.urosjarc.architect.lib

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.*
import com.lemonappdev.konsist.api.declaration.type.KoTypeDeclaration
import com.urosjarc.architect.annotations.*
import com.urosjarc.architect.lib.data.*
import com.urosjarc.architect.lib.domain.*
import com.urosjarc.architect.lib.types.Id
import org.apache.logging.log4j.kotlin.logger
import java.io.InvalidClassException

public object Architect {

    /**
     * TODO: I can't get from parameters and their arguments package paths (this is bug from konsist)
     * for example if you have data class...
     *
     * data class Test(
     *      val test: Id<Employee>
     * )
     *
     * parameter test have type Id from which I can't get package path, and it has type argument Employee for which
     * I can't also get the package path, thats why I need association table...
     */
    private var className_to_package: Map<String, String> = mapOf()

    public fun getStateData(classPackages: Map<String, String>, vararg packages: String): AStateData {
        logger.info("Packages: '${packages.joinToString()}'")


        val classes: List<KoClassDeclaration> = Konsist
            .scopeFromProject()
            .classes()

        val interfaces: List<KoInterfaceDeclaration> = Konsist
            .scopeFromProject()
            .interfaces()

        /** Reset association table... */
        this.className_to_package = classPackages + classes.associate { it.name to it.packagee!!.name } + interfaces.associate { it.name to it.packagee!!.name }

        val identifiers = this.getAnotationEntities(classes, Identifier::class.java)

        return AStateData(
            state = AState(),
            identifiers = identifiers,
            domainEntities = this.getAnotationEntities(classes, DomainEntity::class.java, identifiers = identifiers),
            repos = this.getAnotationEntities(classes, Repository::class.java),
            services = this.getAnotationEntities(classes, Service::class.java),
            useCases = this.getAnotationEntities(classes, UseCase::class.java)
        )
    }

    private fun getClassesFolders(aClassDatas: List<AClassData>, base: List<String>): MutableMap<AClassData, MutableList<String>> {
        if (aClassDatas.isEmpty()) return mutableMapOf()

        /** CALCULATE BASE FOLDERS */
        val packagePaths = aClassDatas.map { it.aClass.packagePath.split(".").toSet() }
        var basePath = packagePaths.first()
        packagePaths.forEach { basePath = basePath.intersect(it) }

        /** CALCULATE DISTINCT FOLDERS */
        val classFolders = mutableMapOf<AClassData, MutableList<String>>()
        aClassDatas.forEach { classFolders[it] = (base + (it.aClass.packagePath.split(".").toSet() - basePath)).toMutableList() }
        return classFolders
    }

    public fun getOrderedDependencies(aStateData: AStateData): List<AClassDataNode> {
        /** GET OBJECT FOLDERS INTO WHICH DEPENDENCIES CAN BE PUTED */
        val classFolders = this.getClassesFolders(aStateData.repos, listOf("repos")) +
                this.getClassesFolders(aStateData.services, listOf("services")) +
                this.getClassesFolders(aStateData.useCases, listOf("usecases"))

        /** GET ALL CLASS NODES */
        val allDependencies = (aStateData.repos + aStateData.services + aStateData.useCases)
            .map { it.aClass.import to AClassDataNode(it, folders = classFolders[it]!!) }.toMap()

        /** CREATE CLASS GRAPH */
        allDependencies.forEach { packagePath, useCase: AClassDataNode ->
            useCase.aClassData.aProps.forEach {
                val dep = allDependencies[it.aProp.type]
                if (dep != null) {
                    useCase.dependencies.add(dep)
                }
            }
        }

        val orderedDependencies = mutableListOf<AClassDataNode>()

        /**  VISIT CLASS GRAPH FROM THE BOTTOM UP */
        repeat(allDependencies.size) {

            /** GET ALL DEPENDENCIES THAT ARE STILL IN ACTIVE STATE AND HAVE ACTIVE DEPENDENCIES */
            val dependencies = allDependencies.filter { it.value.active && it.value.dependencies.filter { it.active }.isEmpty() }

            /** IF NO DEPENDENCIES ARE FOUND WE ARE FINISHED */
            if (dependencies.isEmpty()) return@repeat

            /** IF YOU VISIT AND HANDLE DEPENDENCY THEN MAKE IN NON ACTIVE */
            dependencies.forEach { packagePath, aClassDataNode ->
                orderedDependencies.add(aClassDataNode)
                aClassDataNode.active = false
            }

        }
        return orderedDependencies
    }

    private fun getVisibility(sr: KoClassDeclaration): AVisibility = if (sr.hasPublicOrDefaultModifier) AVisibility.PUBLIC
    else if (sr.hasProtectedModifier) AVisibility.PROTECTED
    else if (sr.hasPrivateModifier) AVisibility.PRIVATE
    else if (sr.hasInternalModifier) AVisibility.INTERNAL
    else throw InvalidClassException("Class '${sr}' has undefined visibility!")

    private fun getVisibility(sr: KoFunctionDeclaration): AVisibility = if (sr.hasPublicOrDefaultModifier) AVisibility.PUBLIC
    else if (sr.hasProtectedModifier) AVisibility.PROTECTED
    else if (sr.hasPrivateModifier) AVisibility.PRIVATE
    else if (sr.hasInternalModifier) AVisibility.INTERNAL
    else throw InvalidClassException("Function '${sr}' has undefined visibility!")

    private fun getTypeParams(returnTypeId: Id<AReturnType>?, paramId: Id<AParam>?, propId: Id<AProp>?, tp: KoTypeDeclaration): List<ATypeParam> =
        tp.name.removeSuffix(">").split("<").last().split(",").map { it.trim() }.map { typeArgument ->

            println("Searching: ${tp.name}")
            val packagePath =
                if (typeArgument.all { it.isUpperCase() } || typeArgument.length == 1) ""
                else this.className_to_package[typeArgument] ?: throw Exception("Can't find class ${typeArgument}")

            ATypeParam(
                paramId = paramId,
                propId = propId,
                returnTypeId = returnTypeId,
                name = typeArgument,
                packagePath = packagePath
            )
        }

    private fun getAnotationEntities(
        scanResult: List<KoClassDeclaration>,
        annotation: Class<out Annotation>,
        identifiers: List<AClassData> = listOf()
    ): List<AClassData> {

        val aEntities = mutableListOf<AClassData>()
        val scanResults: List<KoClassDeclaration> = scanResult.filter { it.annotations.filter { it.name == annotation.simpleName }.isNotEmpty() }

        logger.info("Found ${scanResults.size} @${annotation.simpleName}")


        scanResults.forEach { sr: KoClassDeclaration ->
            println(" * ${sr.path}")

            val aClass = AClass(
                name = sr.name,
                packagePath = sr.fullyQualifiedName!!,
                module = sr.packagee!!.moduleName,
                isAbstract = sr.hasAbstractModifier,
                isData = sr.hasDataModifier,
                isFinal = sr.hasFinalModifier,
                isInner = sr.hasInnerModifier,
                isOpen = sr.hasOpenModifier,
                isSealed = sr.hasSealedModifier,
                isValue = sr.hasValueModifier,
                visibility = this.getVisibility(sr = sr),
            )

            val aEntity = AClassData(
                aClass = aClass,
                aProps = sr.properties(includeNested = false).map { kprop: KoPropertyDeclaration ->
                    val aPropId = Id<AProp>()
                    APropData(
                        aProp = AProp(
                            id = aPropId,
                            classId = aClass.id,
                            name = kprop.name,
                            type = kprop.type!!.bareSourceType,
                            annotations = kprop.annotations.map { it.name },
                            visibility = this.getVisibility(sr = sr),
                            isMutable = kprop.hasVarModifier,
                            isNullable = kprop.type!!.isNullable,
                            isOptional = kprop.hasValue(),
                            isAbstract = kprop.hasAbstractModifier,
                            isConst = kprop.hasConstModifier,
                            isFinal = kprop.hasFinalModifier,
                            isLateinit = kprop.hasLateinitModifier,
                            isOpen = kprop.hasOpenModifier,
                            inlineType = if(kprop.type!!.name.contains("<")) "" else null,
                            isIdentifier = identifiers
                                .map { it.aClass.import == kprop.type!!.name && it.aClass.name.lowercase() == kprop.name }
                                .contains(true)
                        ),
                        aTypeParams = this.getTypeParams(returnTypeId = null, paramId = null, propId = aPropId, tp = kprop.type!!)
                    )
                },
                aConstructor = AConstructor(classId = aClass.id),
                aMethods = sr.functions().filter { fu -> this.getVisibility(fu) == AVisibility.PUBLIC }.map { mfun: KoFunctionDeclaration ->
                    val methodId = Id<AMethod>()
                    val returnTypeId = Id<AReturnType>()
                    AMethodData(
                        aMethod = AMethod(
                            id = methodId,
                            classId = aClass.id,
                            name = mfun.name,
                            visibility = this.getVisibility(mfun)
                        ),
                        aReturnTypeData = AReturnTypeData(
                            aReturnType = AReturnType(
                                id = returnTypeId,
                                type = mfun.returnType!!.bareSourceType,
                                methodId = methodId
                            ),
                            aTypeParams = this.getTypeParams(returnTypeId = returnTypeId, paramId = null, propId = null, tp = mfun.returnType!!)
                        ),
                        aParams = mfun.parameters.map { kparam: KoParameterDeclaration ->
                            val paramId = Id<AParam>()
                            AParamData(
                                aParam = AParam(
                                    id = paramId,
                                    methodId = methodId,
                                    name = kparam.name,
                                    type = kparam.type.bareSourceType,
                                    isOptional = kparam.hasDefaultValue()
                                ),
                                aTypeParams = this.getTypeParams(returnTypeId = null, propId = null, paramId = paramId, tp = kparam.type)
                            )
                        }
                    )
                }
            )
            aEntities.add(aEntity)
        }
        return aEntities
    }

    public fun getFolderNodes(aStateData: AStateData): FolderNode {
        val orderedDependencies = this.getOrderedDependencies(aStateData = aStateData)
        val rootFolder = FolderNode(level = 0)

        orderedDependencies.forEach { it: AClassDataNode ->

            val folders = it.folders
            var currentFolderNode = rootFolder
            while (folders.isNotEmpty()) {
                val folderName = folders.removeFirst()
                val nextFolderNode = currentFolderNode.children.firstOrNull { it.folder == folderName }
                if (nextFolderNode == null) {
                    val folderNode = FolderNode(folder = folderName, level = currentFolderNode.level + 1)
                    currentFolderNode.children.add(folderNode)
                    currentFolderNode = folderNode
                } else {
                    currentFolderNode = nextFolderNode
                }
            }
            currentFolderNode.aClassDatas.add(it.aClassData)
        }

        return rootFolder
    }
}
