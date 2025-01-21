package com.urosjarc.architect.example.interfaces

import com.urosjarc.architect.example.types.Id

public interface Repo<T,N,M> {
    public suspend fun insert(obj: N): T?
    public suspend fun insert(objs: Iterable<N>): List<T> 
    public suspend fun updateMod(obj: M): T?
    public suspend fun update(obj: T): T?
    public suspend fun delete(id: Id<T>): T?
    public suspend fun select(id: Id<T>): T?
    public suspend fun select(): List<T>
}