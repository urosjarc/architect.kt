package com.urosjarc.e2e

import com.urosjarc.architect.core.UseCase


@UseCase
class LoginIn(
    val weatherService: WeatherService,
    val parentRepo: ParentRepo
) {

    fun now(order: String): Boolean {
        //Do something
        //Do another thing

        return true
    }
}

@UseCase
class SignIn(
    val weatherService: WeatherService,
    val parentRepo: ParentRepo
) {
    fun now(): String {
        //Do something
        //Do another thing

        return "OK"
    }
}
