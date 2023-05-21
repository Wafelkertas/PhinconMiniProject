package com.shidqi.phinconminiproject.room

import androidx.room.RoomDatabase
import androidx.room.Database
import com.shidqi.phinconminiproject.models.local.PokemonDetailLocal

@Database(entities = [PokemonDetailLocal::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): PokemonDao
}