package com.shidqi.phinconminiproject.utils

fun getIdFromPokemonUrl(urlString: String): Int? {
    val stripped = urlString.dropLast(1)
    val id = stripped.split("/").last().toIntOrNull()
    var output: Int? = null
    if (id != null) {
        // Do something with the ID
        output = id
    } else {
        // Invalid URL or ID not found

    }
    return output
}
fun getIdFromPokemonUrlLocal(urlString: String): Int? {
    val id = urlString.split("/").last().toIntOrNull()
    var output: Int? = null
    if (id != null) {
        // Do something with the ID
        output = id
    } else {
        // Invalid URL or ID not found

    }
    return output
}

fun generateRenamedName(firstName: String, renameCount: Int?): String {
    val fibonacciNumber = fibonacci(renameCount ?: 0)
    return when (renameCount) {
        null -> firstName
        0 -> "$firstName-0"
        else -> "$firstName-$fibonacciNumber"
    }
}

fun fibonacci(n: Int): Long {
    if (n <= 1) {
        return n.toLong()
    }

    var prev = 0L
    var curr = 1L

    for (i in 2..n) {
        val temp = curr
        curr += prev
        prev = temp
    }

    return curr
}

fun getPokemonImage(pokemonId: Int): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${
        pokemonId
    }.png"
}

fun getPokemonUrlFromImageUrl(url: String): String {
    val startIndex = url.lastIndexOf("/") + 1
    val endIndex = url.lastIndexOf(".")
    val idString = url.substring(startIndex, endIndex)
    return "https://pokeapi.co/api/v2/pokemon/$idString"
}