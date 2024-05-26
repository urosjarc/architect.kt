package com.urosjarc.architect.api.extend

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.primaryConstructor

/**
 * Determines whether the property is mutable or not.
 *
 * @return true if the property is mutable, false otherwise.
 */
internal val <T : Any> KProperty1<T, *>.ext_isMutable: Boolean get() = this is KMutableProperty1<T, *>

/**
 * Returns whether the property is optional or can be nullable.
 *
 * @return true if the property is optional, false otherwise.
 */
internal val <T : Any> KProperty1<T, *>.ext_isOptional: Boolean get() = this.returnType.isMarkedNullable


internal val <T : Any> KProperty1<T, *>.ext_inline: KClass<*>?
    get() {
        val kclass = this.returnType.classifier as KClass<*>
        if (kclass.isValue) {
            val firstParam = kclass.primaryConstructor?.parameters?.firstOrNull()
            return firstParam?.type?.classifier as KClass<*>
        }
        return null
    }
