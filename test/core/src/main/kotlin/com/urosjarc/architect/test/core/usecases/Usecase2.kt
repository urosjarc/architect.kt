package com.urosjarc.architect.test.core.usecases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.test.core.repos.GenreRepo
import com.urosjarc.architect.test.core.repos.InvoiceRepo
import com.urosjarc.architect.test.core.repos.PlaylistRepo
import com.urosjarc.architect.test.core.repos.PlaylistTrackRepo
import com.urosjarc.architect.test.core.services.EmailService

@UseCase
public class Usecase2(
    private val usecase0: Usecase0,
    private val usecase1: Usecase1,
    private val genreRepo: GenreRepo,
    private val playlistTrackRepo: PlaylistTrackRepo,
    private val playlistRepo: PlaylistRepo
) {
    public fun now(): String {
        return "asdf"
    }
}
