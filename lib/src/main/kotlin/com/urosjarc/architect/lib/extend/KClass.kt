package com.urosjarc.architect.lib.extend

import kotlin.reflect.KClass

internal val KClass<*>.ext_name: String get() = this.simpleName!!.toString()

public inline fun <reified T : Any> name(): String = T::class.simpleName.toString()
