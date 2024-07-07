package com.urosjarc.architect.lib.extend

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

/**
 * Retrieve the list of Kotlin function parameters excluding any special kinds.
 *
 * @return The list of function parameters that are of kind VALUE.
 */
internal val KFunction<*>.ext_kparams: List<KParameter> get() = this.parameters.filter { it.kind == KParameter.Kind.VALUE }
