package com.shidqi.phinconminiproject.room

import androidx.room.*
import com.shidqi.phinconminiproject.models.local.PokemonDetailLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(vararg pokemonDetailLocal: PokemonDetailLocal)

    @Delete
    suspend fun deletePokemon( pokemonDetailLocal: PokemonDetailLocal)

    @Query("DELETE FROM pokemon Where id = :id")
    suspend fun queryToDeletePokemon(id: Int)

    @Query("SELECT * FROM pokemon")
    fun getAllPokemon(): Flow<List<PokemonDetailLocal>>

    @Query("SELECT * FROM pokemon Where id = :id")
    suspend fun getPokemon(id: Int): PokemonDetailLocal

    @Update
     fun updatePokemon(pokemonDetailLocal: PokemonDetailLocal)
}