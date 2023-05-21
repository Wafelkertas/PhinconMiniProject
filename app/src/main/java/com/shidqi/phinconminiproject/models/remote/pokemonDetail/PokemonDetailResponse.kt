package com.shidqi.phinconminiproject.models.remote.pokemonDetail

import com.shidqi.phinconminiproject.utils.getPokemonImage

data class PokemonDetailResponse(
    val id: Int,
    val is_default: Boolean,
    val moves: List<Move>,
    val name: String,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Int
){
    val imageUrl : String
        get() = getPokemonImage(this.id)
}