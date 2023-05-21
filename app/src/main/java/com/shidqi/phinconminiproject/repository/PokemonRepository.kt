package com.shidqi.phinconminiproject.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.shidqi.phinconminiproject.data.PokemonPagingSource
import com.shidqi.phinconminiproject.models.local.PokemonDetailLocal
import com.shidqi.phinconminiproject.models.remote.*
import com.shidqi.phinconminiproject.models.remote.pokemonDetail.PokemonDetailResponse
import com.shidqi.phinconminiproject.room.Database
import com.shidqi.phinconminiproject.service.MyServerRetrofitServerInstance
import com.shidqi.phinconminiproject.service.PokeApiRetrofitInstance
import com.shidqi.phinconminiproject.utils.API_PAGE_SIZE
import com.shidqi.phinconminiproject.utils.getPokemonImage
import kotlinx.coroutines.flow.Flow

class PokemonRepository(
    private val pokeApiRetrofitInstance: PokeApiRetrofitInstance,
    private val myServerRetrofitServerInstance: MyServerRetrofitServerInstance,
    private val database: Database
) {

    suspend fun getPokemonList(offset: Int): ListOfPokemonResponse {
        print("getPokemonList")
        return pokeApiRetrofitInstance.getPokemon(offset = offset)
    }

    suspend fun getPokemonDetail(id: Int): PokemonDetailResponse {
        return pokeApiRetrofitInstance.getPokemonDetail(id)
    }

    suspend fun catchPokemon(): CatchPokemonResponse {
        return myServerRetrofitServerInstance.catchPokemon()
    }

    suspend fun release(): ReleasePokemonResponse {
        return myServerRetrofitServerInstance.releasePokemon()
    }

    suspend fun renamePokemon(renamePokemonRequest: RenamePokemonRequest): RenamePokemonResponse {
        return myServerRetrofitServerInstance.renamePokemon(renamePokemonRequest)
    }

    suspend fun insertPokemonToDatabase(id: Int, name: String) {
        database.userDao().insertPokemon(
            PokemonDetailLocal(
                id = id, imageUrl = getPokemonImage(id), name = name
            )
        )
    }

    fun getMyPokemonListFromDB(): Flow<List<PokemonDetailLocal>> {
        return database.userDao().getAllPokemon()
    }

    fun updatePokemon(pokemonDetailLocal: PokemonDetailLocal) {
        database.userDao().updatePokemon(pokemonDetailLocal = pokemonDetailLocal)
    }

    suspend fun deletePokemon(pokemon: PokemonDetailLocal) {
        database.userDao().deletePokemon(pokemonDetailLocal = pokemon)
    }

    suspend fun queryToDeletePokemon(pokemonId: Int) {
        database.userDao().queryToDeletePokemon(id = pokemonId)
    }

    fun getSearchResultStream(): LiveData<PagingData<PokemonData>> {
        return Pager(
            config = PagingConfig(
                pageSize = API_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PokemonPagingSource(this) }
        ).liveData
    }
}