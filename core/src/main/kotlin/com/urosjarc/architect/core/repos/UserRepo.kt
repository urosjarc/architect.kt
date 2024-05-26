package com.urosjarc.architect.core.repos

import com.urosjarc.architect.Repository
import com.urosjarc.architect.core.domain.User
import com.urosjarc.architect.core.types.Id

@Repository<User>
public interface UserRepo {
	public fun getAll(): List<User>
	public fun get(id: Id<User>): User?
	public fun find(email: String): List<User>
	public fun insert(user: User)

}
