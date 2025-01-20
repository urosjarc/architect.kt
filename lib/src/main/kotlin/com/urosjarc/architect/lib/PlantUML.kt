package com.urosjarc.architect.lib

import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.domain.AVisibility
import com.urosjarc.architect.lib.extend.ext_afterLastDot

internal object PlantUML {
    fun getMethods(entity: AClassData): List<String> {
        return buildList {
            entity.aMethods.forEach { m ->

                var returnType = m.aReturnTypeData.aReturnType.type.ext_afterLastDot
                val returnTypeArg = m.aReturnTypeData.aTypeParams.map { it.name }
                if(returnTypeArg.isNotEmpty()) returnType += "<${returnTypeArg.joinToString(",")}>"
                val params = buildList {
                    m.aParams.forEach { p ->
                        var paramType = p.aParam.type.ext_afterLastDot
                        if (p.aTypeParams.isNotEmpty()) paramType += "<${p.aTypeParams.joinToString { it.name }}>"
                        this.add("${p.aParam.name}: $paramType")
                    }
                }.joinToString()

                this.add("\t${getVisibility(m.aMethod.visibility)}${m.aMethod.name}($params) : $returnType")
            }
        }
    }

    internal fun getVisibility(aVisibility: AVisibility): Char {
        return when (aVisibility) {
            AVisibility.PUBLIC -> '+'
            AVisibility.PROTECTED -> '#'
            AVisibility.INTERNAL -> '~'
            AVisibility.PRIVATE -> '-'
        }
    }
}
