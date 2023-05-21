package com.shidqi.phinconminiproject.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shidqi.phinconminiproject.models.remote.PokemonData
import com.shidqi.phinconminiproject.repository.PokemonRepository
import com.shidqi.phinconminiproject.room.Database
import com.shidqi.phinconminiproject.utils.API_PAGE_SIZE
import com.shidqi.phinconminiproject.utils.STARTING_OFFSET
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class PokemonPagingSource @Inject constructor(private val pokemonRepository: PokemonRepository ) : PagingSource<Int, PokemonData>() {

    override fun getRefreshKey(state: PagingState<Int, PokemonData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
        Log.d("getRefreshKey","anchorPosition=$anchorPosition")
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonData> {

        val position = params.key ?: STARTING_OFFSET
        Log.d("getRefreshKeyload","position=$position")
        return try {
            val response = pokemonRepository.getPokemonList(position * API_PAGE_SIZE)
            val listOfPokemon = response.results

            val nextKey = if (listOfPokemon.isEmpty()) {
                null
            } else {
                position + (params.loadSize / API_PAGE_SIZE)
            }
        Log.d("getRefreshKeyload","position=$position nextKey=$nextKey")
            LoadResult.Page(
                data = listOfPokemon,
                prevKey = if (position == STARTING_OFFSET) null else position - API_PAGE_SIZE,
                nextKey = nextKey
            )

        }catch (exception: IOException) {
            Log.d("exception", "httpException")
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.d("exception", "ioException")
            return LoadResult.Error(exception)
        }
    }
}