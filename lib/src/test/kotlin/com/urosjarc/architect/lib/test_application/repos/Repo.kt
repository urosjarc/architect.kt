package com.urosjarc.architect.lib.test_application.repos

import com.urosjarc.architect.lib.types.Id

interface Repo<T> {
    fun getOneById(id: Id<T>): T
    fun getAll(): List<T>
    fun create(obj: T): List<T>
    fun delete(obj: T): Boolean

}
