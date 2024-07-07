package com.urosjarc.architect.lib.extend

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

/**
 * Retrieves the list of constructor parameters [KParameter] for the given class [KClass].
 *
 * @return The list of constructor parameters for the given class, or null if the class does not have a primary constructor.
 */
internal val KClass<*>.ext_kparams: List<KParameter>
    get() = this.primaryConstructor?.parameters?.filter { it.kind == KParameter.Kind.VALUE } ?: listOf() // { INSTANCE, EXTENSION_RECEIVER, VALUE }

internal val KClass<*>.ext_kfunctions: List<KFunction<*>>
    get() = this.memberFunctions.filter { m ->
        val nameIsValid = !m.name.contains("[$0-9]".toRegex())
        val isNotDefault = !listOf("constructor", "copy", "equals", "toString", "hashCode").contains(m.name)
        nameIsValid && isNotDefault
    }

/**
 * Retrieves the list of properties [KProperty1] for a given class [KClass].
 *
 * @param kclass The class for which to retrieve the properties.
 * @return The list of properties for the given class.
 */
internal val KClass<*>.ext_kprops: List<KProperty1<out Any, *>> get() = this.memberProperties.filter { it.javaField != null }
internal val KClass<*>.ext_name: String get() = this.simpleName!!.toString()

public inline fun <reified T : Any> name(): String = T::class.simpleName.toString()
