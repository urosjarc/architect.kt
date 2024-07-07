package com.urosjarc.architect.lib

import com.urosjarc.architect.annotations.*
import com.urosjarc.architect.lib.data.*
import com.urosjarc.architect.lib.domain.*
import com.urosjarc.architect.lib.extend.*
import com.urosjarc.architect.lib.types.Id
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
import org.apache.logging.log4j.kotlin.logger
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1

public object Architect {

    public fun getStateData(vararg packages: String): AStateData {
        logger.info("Packages: '${packages.joinToString()}'")

        val scanResult = ClassGraph()
            .acceptPackages(*packages)
            .enableAllInfo()
            .verbose()
            .scan()

        val identifiers = this.getAnotationEntities(scanResult, Identifier::class.java)

        return AStateData(
            state = AState(),
            identifiers = identifiers,
            domainEntities = this.getAnotationEntities(scanResult, DomainEntity::class.java, identifiers = identifiers),
            repos = this.getAnotationEntities(scanResult, Repository::class.java),
            services = this.getAnotationEntities(scanResult, Service::class.java),
            useCases = this.getAnotationEntities(scanResult, UseCase::class.java)
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

    private fun getAnotationEntities(
        scanResult: ScanResult,
        annotation: Class<out Annotation>,
        identifiers: List<AClassData> = listOf()
    ): List<AClassData> {

        val aEntities = mutableListOf<AClassData>()
        val scanResults = scanResult.getClassesWithAnnotation(annotation)

        logger.info("Found ${scanResults.size} @${annotation.simpleName}")
        scanResults.forEach { logger.info(" * ${it.name}") }

        scanResults.forEach { sr: ClassInfo ->

            //TODO: https://youtrack.jetbrains.com/issue/KT-10440
            val kclass = Class.forName(sr.name).kotlin

            val aClass = AClass(
                name = kclass.ext_name,
                packagePath = sr.packageName,
                module = sr.moduleInfo?.name,
                isAbstract = kclass.isAbstract,
                isCompanion = kclass.isCompanion,
                isData = kclass.isData,
                isFinal = kclass.isFinal,
                isFun = kclass.isFun,
                isInner = kclass.isInner,
                isOpen = kclass.isOpen,
                isSealed = kclass.isSealed,
                isValue = kclass.isValue,
                visibility = AVisibility.valueOf(kclass.visibility!!.name),
            )

            val aEntity = AClassData(
                aClass = aClass,
                aProps = kclass.ext_kprops.map { kprop: KProperty1<out Any, *> ->
                    val aPropId = Id<AProp>()
                    val type = kprop.returnType.toString().split("<").first()
                    APropData(
                        aProp = AProp(
                            id = aPropId,
                            classId = aClass.id,
                            name = kprop.name,
                            type = type,
                            annotations = kclass.ext_kparams
                                .firstOrNull { it.name == kprop.name }?.annotations
                                ?.map { it.annotationClass.ext_name } ?: listOf(),
                            inlineType = kprop.ext_inline?.toString(),
                            visibility = AVisibility.valueOf(kprop.visibility!!.name),
                            isMutable = kprop.ext_isMutable,
                            isNullable = kprop.ext_isNullable,
                            isOptional = kclass.isData && kclass.ext_kparams.firstOrNull { it.name == kprop.name }?.isOptional ?: false,
                            isAbstract = kprop.isAbstract,
                            isConst = kprop.isConst,
                            isFinal = kprop.isFinal,
                            isLateinit = kprop.isLateinit,
                            isOpen = kprop.isOpen,
                            isSuspend = kprop.isSuspend,
                            isIdentifier = identifiers.map { it.aClass.import == type && it.aClass.name.lowercase() == kprop.name }.contains(true)
                        ),
                        aTypeParams = kprop.returnType.arguments.map { arg ->
                            ATypeParam(
                                paramId = null,
                                propId = aPropId,
                                returnTypeId = null,
                                name = arg.toString().afterLastDot,
                                packagePath = arg.toString().beforeLastDot
                            )
                        }
                    )
                },
                aConstructor = AConstructor(classId = aClass.id),
                aMethods = kclass.ext_kfunctions.map { mfun: KFunction<*> ->
                    val methodId = Id<AMethod>()
                    val returnTypeId = Id<AReturnType>()
                    AMethodData(
                        aMethod = AMethod(
                            id = methodId,
                            classId = aClass.id,
                            name = mfun.name,
                            visibility = AVisibility.valueOf(mfun.visibility!!.name)
                        ),
                        aReturnTypeData = AReturnTypeData(
                            aTypeParams = mfun.returnType.arguments.map {
                                val returnType = mfun.returnType.arguments.first().type.toString()
                                ATypeParam(
                                    propId = null,
                                    paramId = null,
                                    returnTypeId = returnTypeId,
                                    name = returnType.afterLastDot,
                                    packagePath = returnType.beforeLastDot
                                )
                            },
                            aReturnType = AReturnType(
                                id = returnTypeId,
                                type = mfun.returnType.toString(),
                                methodId = methodId
                            ),
                        ),
                        aParams = mfun.ext_kparams.map { kparam ->
                            val type = kparam.type.toString().removePrefix("class ")
                            val infos = type.removeSuffix(">").split("<")
                            val typeParams = if (infos.size == 2) infos.last().split(",") else listOf()
                            val paramId = Id<AParam>()
                            AParamData(
                                aParam = AParam(
                                    id = paramId,
                                    methodId = methodId,
                                    name = kparam.name.toString(),
                                    type = infos.first(),
                                    kind = kparam.kind.name,
                                    isOptional = kparam.isOptional,
                                ),
                                aTypeParams = typeParams.map { tp ->
                                    ATypeParam(
                                        propId = null,
                                        returnTypeId = null,
                                        paramId = paramId,
                                        name = tp.afterLastDot,
                                        packagePath = infos.last().beforeLastDot
                                    )
                                },
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
