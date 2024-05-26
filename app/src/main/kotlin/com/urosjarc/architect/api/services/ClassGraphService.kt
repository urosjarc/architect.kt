package com.urosjarc.architect.api.services

import com.urosjarc.architect.DomainEntity
import com.urosjarc.architect.Repository
import com.urosjarc.architect.Service
import com.urosjarc.architect.UseCase
import com.urosjarc.architect.api.extend.*
import com.urosjarc.architect.core.domain.*
import com.urosjarc.architect.core.services.ClassService
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import kotlin.reflect.KClass

internal class ClassGraphService : ClassService {
    private fun getEntities(scanResult: ScanResult, anotation: Class<out Annotation>): List<AEntity> {
        val aEntities = mutableListOf<AEntity>()
        scanResult.getClassesWithAnnotation(anotation).forEach {
            val kclass = Class.forName(it.name).kotlin //TODO: https://youtrack.jetbrains.com/issue/KT-10440
            val aClass = AClass(
                name = kclass.simpleName!!,
                path = it.packageName
            )
            val aEntity = AEntity(
                aClass = aClass,
                aProps = kclass.ext_kprops.map {
                    AProp(
                        classId = aClass.id,
                        name = it.name,
                        type = (it.returnType.classifier as KClass<*>).simpleName!!,
                        inlineType = it.ext_inline?.simpleName,
                        visibility = it.visibility!!.name,
                        isMutable = it.ext_isMutable,
                        isOptional = it.ext_isOptional,
                        isAbstract = it.isAbstract,
                        isConst = it.isConst,
                        isFinal = it.isFinal,
                        isLateinit = it.isLateinit,
                        isOpen = it.isOpen,
                        isSuspend = it.isSuspend,
                    )
                },
                aParams = kclass.ext_kparams.map {
                    AParam(
                        classId = aClass.id,
                        name = it.name!!,
                        type = (it.type.classifier as KClass<*>).simpleName!!,
                        isOptional = it.isOptional,
                        kind = it.kind.name,
                    )
                }
            )
            aEntities.add(aEntity)
        }
        return aEntities
    }

    override fun getState(packagePath: String): AState {
        val scanResult = ClassGraph().verbose().enableAllInfo().acceptPackages(packagePath).scan()

        return AState(
            domainEntities = getEntities(scanResult, DomainEntity::class.java),
            repos = getEntities(scanResult, Repository::class.java),
            services = getEntities(scanResult, Service::class.java),
            useCases = getEntities(scanResult, UseCase::class.java)
        )
    }
}
