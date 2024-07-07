package com.urosjarc.architect.test.core.usecases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.test.core.repos.AlbumRepo
import com.urosjarc.architect.test.core.services.JsonService

@UseCase
public class Usecase0(
    private val jsonService: JsonService,
    private val albumRepo: AlbumRepo
) {
    public fun now() {

    }
}
