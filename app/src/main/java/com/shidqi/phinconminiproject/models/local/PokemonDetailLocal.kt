package com.shidqi.phinconminiproject.models.local

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shidqi.phinconminiproject.models.remote.PokemonData
import com.shidqi.phinconminiproject.utils.getPokemonUrlFromImageUrl

@Entity(tableName = "pokemon")
data class PokemonDetailLocal(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "rename_count") val rename_count: Int? = null,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "name") val name: String,
)

fun PokemonDetailLocal.toPokemonData() : PokemonData{
    Log.d("toPokemonData", getPokemonUrlFromImageUrl(this.imageUrl))
    return PokemonData(name = this.name, url = getPokemonUrlFromImageUrl(this.imageUrl))
}

