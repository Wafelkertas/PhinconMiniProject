package com.shidqi.phinconminiproject.models.remote

data class RenamePokemonRequest(
    val name: String,
    val renameCount: Int
)