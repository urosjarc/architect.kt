package com.urosjarc.architect.test.core.usecases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.test.core.services.JsonService

@UseCase
class Usecase0(
    private val jsonService: JsonService
) {
    public fun now() {

    }
}

@UseCase
class Usecase1(
    private val jsonService: JsonService,
    private val usecase0: Usecase0
) {
    public fun now() {

    }
}

@UseCase
class Usecase2(
    private val jsonService: JsonService,
    private val usecase0: Usecase0,
    private val usecase1: Usecase1
) {
    public fun now() {

    }
}
