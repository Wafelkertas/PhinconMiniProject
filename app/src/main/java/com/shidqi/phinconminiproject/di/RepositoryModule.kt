package com.shidqi.phinconminiproject.di

import com.shidqi.phinconminiproject.service.PokeApiRetrofitInstance
import com.shidqi.phinconminiproject.repository.PokemonRepository
import com.shidqi.phinconminiproject.room.Database
import com.shidqi.phinconminiproject.service.MyServerRetrofitServerInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Repository module instantiate as singleton so it will live as long the app running
     * **/
    @Singleton
    @Provides
    fun providePokemonRepository(retrofitService: PokeApiRetrofitInstance,myServerRetrofitService: MyServerRetrofitServerInstance, database: Database): PokemonRepository {
        return PokemonRepository(retrofitService, myServerRetrofitService, database)
    }


}