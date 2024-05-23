package com.urosjarc.e2e

import com.urosjarc.architect.core.Repository


@Repository<Parent>
interface ParentRepo {
    fun get(id: Id<Parent>): Parent
    fun getAll(): List<Parent>
}

class ParentSqlRepo: ParentRepo {
    override fun get(id: Id<Parent>): Parent {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Parent> {
        TODO("Not yet implemented")
    }

}

@Repository<Child>
interface ChildRepo {
    fun get(id: Id<Child>): Child
    fun getAll(): List<Child>
}


class ChildSqlRepo: ChildRepo {
    override fun get(id: Id<Child>): Child {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Child> {
        TODO("Not yet implemented")
    }

}
