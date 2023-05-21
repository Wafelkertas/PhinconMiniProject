package com.shidqi.phinconminiproject.models.remote

import com.shidqi.phinconminiproject.models.local.PokemonDetailLocal
import com.shidqi.phinconminiproject.utils.getIdFromPokemonUrl

data class ListOfPokemonResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<PokemonData>
)

data class PokemonData(
    val name: String,
    val url: String,

    ) {
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${
            getIdFromPokemonUrl(
                this.url
            )
        }.png"
    val id: Int
        get() = getIdFromPokemonUrl(this.url) ?: 0




}

