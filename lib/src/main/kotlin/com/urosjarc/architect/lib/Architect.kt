package com.urosjarc.architect.lib

import com.urosjarc.architect.annotations.*
import com.urosjarc.architect.lib.data.*
import com.urosjarc.architect.lib.domain.*
import com.urosjarc.architect.lib.extend.*
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.ScanResult
import org.apache.logging.log4j.kotlin.logger
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberFunctions

public object Architect {

    public fun getStateData(vararg packages: String): AStateData {
        logger.info("Packages: '${packages.joinToString()}'")

        val scanResult = ClassGraph()
            .acceptPackages(*packages)
            .enableAllInfo()
            .verbose()
            .scan()

        return AStateData(
            state = AState(),
            identifiers = this.getAnotationEntities(scanResult, Identifier::class.java),
            domainEntities = this.getAnotationEntities(scanResult, DomainEntity::class.java),
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

    private fun getAnotationEntities(scanResult: ScanResult, annotation: Class<out Annotation>): List<AClassData> {
        val aEntities = mutableListOf<AClassData>()
        val scanResults = scanResult.getClassesWithAnnotation(annotation)

        logger.info("Found ${scanResults.size} @${annotation.simpleName}")
        scanResults.forEach { logger.info(" * ${it.name}") }

        scanResults.forEach { sr: ClassInfo ->

            //TODO: https://youtrack.jetbrains.com/issue/KT-10440
            val kclass = Class.forName(sr.name).kotlin

            val aClass = AClass(
                name = kclass.simpleName!!,
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

            val constructor = AMethod(
                classId = aClass.id,
                type = AMethod.Type.CONSTRUCTOR,
                name = "constructor",
                returnType = null,
                visibility = AVisibility.PUBLIC
            )

            val aEntity = AClassData(
                aClass = aClass,
                aProps = kclass.ext_kprops.map { kprop: KProperty1<out Any, *> ->
                    val typeInfos = kprop.returnType.toString().removeSuffix(">").split("<")
                    val type = typeInfos.first()
                    val typeParams = typeInfos.last().split(",")

                    val annotations = kclass.ext_kparams
                        .firstOrNull { it.name == kprop.name }?.annotations
                        ?.map { it.annotationClass.simpleName.toString() }

                    val aProp = AProp(
                        classId = aClass.id,
                        name = kprop.name,
                        type = type,
                        annotations = annotations ?: listOf(),
                        inlineType = kprop.ext_inline?.toString(),
                        visibility = AVisibility.valueOf(kprop.visibility!!.name),
                        isMutable = kprop.ext_isMutable,
                        isOptional = kprop.ext_isOptional,
                        isAbstract = kprop.isAbstract,
                        isConst = kprop.isConst,
                        isFinal = kprop.isFinal,
                        isLateinit = kprop.isLateinit,
                        isOpen = kprop.isOpen,
                        isSuspend = kprop.isSuspend,
                    )
                    APropData(
                        aProp = aProp,
                        aTypeParams = typeParams.map {
                            val clsName = it.split(".").last()
                            ATypeParam(
                                propId = aProp.id,
                                name = clsName,
                                packagePath = it.removeSuffix(".$clsName")
                            )
                        }
                    )
                },
                aMethods = listOf(
                    AMethodData(
                        aMethod = constructor,
                        aParams = kclass.ext_kparams.map { kparam ->
                            AParam(
                                methodId = constructor.id,
                                name = kparam.name.toString(),
                                type = kparam.type.toString().removePrefix("class "),
                                kind = kparam.kind.name,
                                isOptional = kparam.isOptional,
                            )
                        }
                    )
                ) + kclass.memberFunctions.map { mfun ->
                    val aMethod = AMethod(
                        classId = aClass.id,
                        type = AMethod.Type.METHOD,
                        name = mfun.name,
                        returnType = mfun.returnType.toString(),
                        visibility = AVisibility.valueOf(mfun.visibility!!.name)
                    )
                    AMethodData(
                        aMethod = aMethod,
                        aParams = mfun.parameters.map { kparam ->
                            AParam(
                                methodId = aMethod.id,
                                name = kparam.name.toString(),
                                type = kparam.type.toString().removePrefix("class "),
                                kind = kparam.kind.name,
                                isOptional = kparam.isOptional,
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
