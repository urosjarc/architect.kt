package com.urosjarc.architect.lib

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.*
import com.lemonappdev.konsist.api.declaration.type.KoTypeDeclaration
import com.urosjarc.architect.annotations.*
import com.urosjarc.architect.lib.data.*
import com.urosjarc.architect.lib.domain.*
import com.urosjarc.architect.lib.extend.ext_beforeLastDot
import com.urosjarc.architect.lib.types.Id
import org.apache.logging.log4j.kotlin.logger
import java.io.InvalidClassException

public object Architect {
    private lateinit var classMapping: ClassMapping

    public fun getStateData(scannedPackage: String, classPackages: Map<String, String>): AStateData {
        this.classMapping = ClassMapping(classPackages = classPackages.toMutableMap())

        val classesAndEnums: List<KoClassDeclaration> = Konsist.scopeFromProject().classes()
            .filter { it.packagee!!.name.startsWith(scannedPackage) }

        val interfaces: List<KoInterfaceDeclaration> = Konsist.scopeFromProject().interfaces()
            .filter { it.packagee!!.name.startsWith(scannedPackage) }

        val classes = classesAndEnums.filter { !it.hasEnumModifier }
        val enums = classesAndEnums.filter { it.hasEnumModifier }

        /** Reset association table... */
        (classesAndEnums + interfaces).forEach { cls ->
            val key = cls.name
            val value = cls.packagee!!.name
            this.classMapping.setPackage(className = key, packagePath = value)
        }

        val identifiers = this.getAnnotationEntities(classes, Identifier::class.java)

        return AStateData(
            state = AState(),
            identifiers = identifiers,
            domainEntities = this.getAnnotationEntities(classes, DomainEntity::class.java, identifiers = identifiers),
            domainValues = this.getAnnotationEnums(enums, DomainValues::class.java),
            repos = this.getAnnotationEntities(classes, Repository::class.java),
            services = this.getAnnotationEntities(classes, Service::class.java),
            useCases = this.getAnnotationEntities(classes, UseCase::class.java)
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

    private fun getTypeParams(returnTypeId: Id<AReturnType>?, paramId: Id<AParam>?, propId: Id<AProp>?, tp: KoTypeDeclaration): List<ATypeParam> {
        if (!tp.name.endsWith(">")) return listOf()
        return tp.name.removeSuffix(">").split("<").last().split(",").map { it.trim() }.map { typeArgument ->

            val packagePath =
                if (typeArgument.all { it.isUpperCase() } || typeArgument.length == 1) ""
                else this.classMapping.getPackage(className = typeArgument)

            ATypeParam(
                paramId = paramId,
                propId = propId,
                returnTypeId = returnTypeId,
                name = typeArgument,
                packagePath = packagePath
            )
        }
    }

    private fun getAnnotationEnums(scanResult: List<KoClassDeclaration>, annotation: Class<out Annotation>): List<AEnumData> {

        val aEntities = mutableListOf<AEnumData>()
        val scanResults: List<KoClassDeclaration> = scanResult.filter { it.annotations.filter { it.name == annotation.simpleName }.isNotEmpty() }

        logger.info("Found ${scanResults.size} @${annotation.simpleName}")

        scanResults.forEach { sr: KoClassDeclaration ->
            val aEnum = AEnum(
                name = sr.name,
                packagePath = sr.fullyQualifiedName!!.ext_beforeLastDot,
                module = sr.packagee!!.moduleName,
                visibility = this.getVisibility(sr = sr),
                type = sr.primaryConstructor?.parameters?.first()?.type?.bareSourceType,
            )

            val aEntity = AEnumData(
                aEnum = aEnum,
                aEnumValues = sr.enumConstants.map { ec: KoEnumConstantDeclaration ->
                    val value = if (ec.text.contains("(")) ec.text.substringAfter("(").substringBefore(")").replace("\"", "").trim() else null
                    AEnumValue(name = ec.name, value = value)
                }
            )
            aEntities.add(aEntity)
        }
        return aEntities
    }

    private fun getAnnotationEntities(
        scanResult: List<KoClassDeclaration>,
        annotation: Class<out Annotation>,
        identifiers: List<AClassData> = listOf()
    ): List<AClassData> {

        val aEntities = mutableListOf<AClassData>()
        val scanResults: List<KoClassDeclaration> = scanResult.filter { it.annotations.filter { it.name == annotation.simpleName }.isNotEmpty() }

        logger.info("Found ${scanResults.size} @${annotation.simpleName}")


        scanResults.forEach { sr: KoClassDeclaration ->
            val aClass = AClass(
                name = sr.name,
                packagePath = sr.fullyQualifiedName!!.ext_beforeLastDot,
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
                    val isIdentifier = identifiers
                        .map { iden ->
                            val kpropType = kprop.type!!.bareSourceType
                            val kpropImport = "${this.classMapping.getPackage(className = kpropType)}.${kpropType}"
                            // Import and parameter name must match with one of the identifier
                            iden.aClass.import == kpropImport && iden.aClass.name.lowercase() == kprop.name
                        }
                        .contains(true)
                    val type = kprop.type!!.bareSourceType
                    APropData(
                        aProp = AProp(
                            id = aPropId,
                            classId = aClass.id,
                            name = kprop.name,
                            type = type,
                            packagePath = this.classMapping.getPackage(className = type),
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
                            inlineType = if (kprop.type!!.name.contains("<")) "" else null,
                            isIdentifier = isIdentifier
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
