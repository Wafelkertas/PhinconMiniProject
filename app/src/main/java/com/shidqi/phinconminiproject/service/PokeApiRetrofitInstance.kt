package com.shidqi.phinconminiproject.service

import com.shidqi.phinconminiproject.models.remote.*
import com.shidqi.phinconminiproject.models.remote.pokemonDetail.PokemonDetailResponse
import com.shidqi.phinconminiproject.utils.API_PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiRetrofitInstance {

    @GET("pokemon/")
    suspend fun getPokemon(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = API_PAGE_SIZE
    ): ListOfPokemonResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailResponse



}