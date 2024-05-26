package com.urosjarc.architect.api.services

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.annotations.Repository
import com.urosjarc.architect.annotations.Service
import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.api.extend.*
import com.urosjarc.architect.core.data.AClassData
import com.urosjarc.architect.core.data.AMethodData
import com.urosjarc.architect.core.domain.*
import com.urosjarc.architect.core.services.ClassService
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import kotlin.reflect.full.memberFunctions

internal class Architect : ClassService {

    override fun getState(packagePath: String): AState {
        val scanResult = ClassGraph().enableAllInfo().acceptPackages(packagePath).scan()

        return AState(
            domainEntities = getEntities(scanResult, DomainEntity::class.java),
            repos = getEntities(scanResult, Repository::class.java),
            services = getEntities(scanResult, Service::class.java),
            useCases = getEntities(scanResult, UseCase::class.java)
        )
    }

    private fun getEntities(scanResult: ScanResult, anotation: Class<out Annotation>): List<AClassData> {
        val aEntities = mutableListOf<AClassData>()
        scanResult.getClassesWithAnnotation(anotation).forEach { sr ->

            val kclass = Class.forName(sr.name).kotlin //TODO: https://youtrack.jetbrains.com/issue/KT-10440

            val aClass = AClass(
                name = kclass.simpleName!!,
                path = sr.packageName,
                module = sr.moduleInfo.name
            )

            val constructor = AMethod(
                classId = aClass.id,
                type = AMethod.Type.CONSTRUCTOR,
                name = "constructor",
            )

            val aEntity = AClassData(
                aClass = aClass,
                aProps = kclass.ext_kprops.map { kprop ->
                    AProp(
                        classId = aClass.id,
                        name = kprop.name,
                        type = kprop.returnType.toString(),
                        inlineType = kprop.ext_inline?.toString(),
                        visibility = kprop.visibility!!.name,
                        isMutable = kprop.ext_isMutable,
                        isOptional = kprop.ext_isOptional,
                        isAbstract = kprop.isAbstract,
                        isConst = kprop.isConst,
                        isFinal = kprop.isFinal,
                        isLateinit = kprop.isLateinit,
                        isOpen = kprop.isOpen,
                        isSuspend = kprop.isSuspend,
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
                        name = mfun.name
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
}
