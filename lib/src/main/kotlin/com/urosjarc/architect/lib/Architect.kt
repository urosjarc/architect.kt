
import com.urosjarc.architect.annotations.*
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.data.AMethodData
import com.urosjarc.architect.lib.data.APropData
import com.urosjarc.architect.lib.domain.*
import com.urosjarc.architect.lib.extend.*
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import kotlin.reflect.KCallable
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

public object Architect {

    public fun getState(vararg packagePath: String): AState {
        val scanResult = ClassGraph().enableAllInfo().acceptPackages(*packagePath).scan()

        return AState(
            identifiers = getAnotationEntities(scanResult, Identifier::class.java),
            domainEntities = getAnotationEntities(scanResult, DomainEntity::class.java),
            repos = getAnotationEntities(scanResult, Repository::class.java),
            services = getAnotationEntities(scanResult, Service::class.java),
            useCases = getAnotationEntities(scanResult, UseCase::class.java)
        )
    }

    private fun getAnotationEntities(scanResult: ScanResult, annotation: Class<out Annotation>): List<AClassData> {
        val aEntities = mutableListOf<AClassData>()
        scanResult.getClassesWithAnnotation(annotation).forEach { sr ->

            //TODO: https://youtrack.jetbrains.com/issue/KT-10440
            val kclass = Class.forName(sr.name).kotlin

            val aClass = AClass(
                name = kclass.simpleName!!,
                path = sr.packageName,
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
                visibility = AVisibility.PUBLIC
            )

            val aEntity = AClassData(
                aClass = aClass,
                aProps = kclass.ext_kprops.map { kprop: KProperty1<out Any, *> ->
                    val typeInfos = kprop.returnType.toString().removeSuffix(">").split("<")
                    val type = typeInfos.first()
                    val typeParams = typeInfos.last().split(",")
                    val aProp = AProp(
                        classId = aClass.id,
                        name = kprop.name,
                        type = type,
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
                                path = it.removeSuffix(".$clsName")
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

}
