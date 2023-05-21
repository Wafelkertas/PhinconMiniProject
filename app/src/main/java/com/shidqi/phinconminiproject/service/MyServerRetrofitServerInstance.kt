package com.shidqi.phinconminiproject.service

import com.shidqi.phinconminiproject.models.remote.CatchPokemonResponse
import com.shidqi.phinconminiproject.models.remote.ReleasePokemonResponse
import com.shidqi.phinconminiproject.models.remote.RenamePokemonRequest
import com.shidqi.phinconminiproject.models.remote.RenamePokemonResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface MyServerRetrofitServerInstance {


    @GET("pokemon/catch")
    suspend fun catchPokemon(): CatchPokemonResponse

    @GET("pokemon/release")
    suspend fun releasePokemon(
    ): ReleasePokemonResponse

    @PUT("pokemon/rename")
    suspend fun renamePokemon(
        @Body request: RenamePokemonRequest
    ): RenamePokemonResponse
}