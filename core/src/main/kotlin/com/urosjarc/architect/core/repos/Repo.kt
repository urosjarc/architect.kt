package com.urosjarc.architect.core.repos

import com.urosjarc.architect.core.types.Id

public interface Repo<T> {

    public fun getAll(): List<T>

    public fun get(id: Id<T>): T?

    public fun insert(user: T)
}
