package com.gabe.navigateapplication.network

import retrofit2.http.GET
import retrofit2.http.Query

interface RetroService {

    //https://rickandmortyapi.com/api/character?page=1
    @GET( "character")
    suspend fun getDataFromApi(@Query("page") query:Int ):RickAndMortyList
}