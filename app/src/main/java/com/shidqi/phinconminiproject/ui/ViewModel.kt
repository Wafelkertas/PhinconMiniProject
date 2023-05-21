package com.shidqi.phinconminiproject.ui

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModel
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.shidqi.phinconminiproject.models.local.PokemonDetailLocal
import com.shidqi.phinconminiproject.models.remote.CatchPokemonResponse
import com.shidqi.phinconminiproject.models.remote.ReleasePokemonResponse
import com.shidqi.phinconminiproject.models.remote.RenamePokemonRequest
import com.shidqi.phinconminiproject.models.remote.RenamePokemonResponse
import com.shidqi.phinconminiproject.models.remote.pokemonDetail.PokemonDetailResponse
import com.shidqi.phinconminiproject.repository.PokemonRepository
import com.shidqi.phinconminiproject.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(private val pokemonRepository: PokemonRepository) :
    ViewModel() {
    val data = pokemonRepository.getSearchResultStream().cachedIn(viewModelScope);
    val pokemonDetail: MutableLiveData<Resource<PokemonDetailResponse>> by lazy {
        MutableLiveData<Resource<PokemonDetailResponse>>()
    }
    val catchPokemonResponse: MutableLiveData<Resource<CatchPokemonResponse>> by lazy {
        MutableLiveData<Resource<CatchPokemonResponse>>(Resource.Idle())
    }
    val releasePokemonResponse: MutableLiveData<Resource<ReleasePokemonResponse>> by lazy {
        MutableLiveData<Resource<ReleasePokemonResponse>>(Resource.Idle())
    }

    val renamePokemonResponse: MutableLiveData<Resource<RenamePokemonResponse>> by lazy {
        MutableLiveData<Resource<RenamePokemonResponse>>(Resource.Idle())
    }
    val myPokemonList: LiveData<List<PokemonDetailLocal>> =
        pokemonRepository.getMyPokemonListFromDB().asLiveData(Dispatchers.IO)


    fun getPokemonDetail(id: Int) {
        viewModelScope.launch {
            pokemonDetail.value = Resource.Loading()
            try {
                val response = pokemonRepository.getPokemonDetail(id = id)
                pokemonDetail.value = Resource.Success(response)
            } catch (e: HttpException) {
                pokemonDetail.value = Resource.Error(e.toString())
            }catch (e: IOException) {
                pokemonDetail.value = Resource.Error(e.toString())
            }
        }
    }

    private fun insertPokemonIntoDB(id: Int, name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            pokemonRepository.insertPokemonToDatabase(id = id, name = name)
        }
    }

    fun loadMyPokemon(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            pokemonRepository.getMyPokemonListFromDB()
        }
    }

    fun updateMyPokemon(pokemonDetailLocal: PokemonDetailLocal) {
        CoroutineScope(Dispatchers.IO).launch {
            pokemonRepository.updatePokemon(pokemonDetailLocal = pokemonDetailLocal)
        }
    }

    fun catchPokemon(id: Int, name: String) {
        viewModelScope.launch {
            try {
                catchPokemonResponse.value = Resource.Loading()
                val response = pokemonRepository.catchPokemon()
                catchPokemonResponse.value = Resource.Success(response)
                Log.d("catchPokemon", response.toString())
                if (response.probability == 1) {
                    insertPokemonIntoDB(id, name)

                }
            } catch (e: HttpException) {
                catchPokemonResponse.value = Resource.Error(e.toString())
            }catch (e: IOException) {
                catchPokemonResponse.value = Resource.Error(e.toString())
            }
        }
    }

    fun releasePokemon(id: Int) {
        viewModelScope.launch {
            try {
                releasePokemonResponse.value = Resource.Loading()
                val response = pokemonRepository.release()
                releasePokemonResponse.value = Resource.Success(response)
                Log.d("releasePokemon", response.toString())
                if (response.success) {
                    CoroutineScope(Dispatchers.IO).launch {
                        pokemonRepository.queryToDeletePokemon(pokemonId = id)
                    }
                }

            } catch (e: HttpException) {
                releasePokemonResponse.value = Resource.Error(e.toString())
            }catch (e: IOException) {
                releasePokemonResponse.value = Resource.Error(e.toString())
            }
        }
    }

    fun rename(local: PokemonDetailLocal) {
        viewModelScope.launch {
            try {
                renamePokemonResponse.value = Resource.Loading()
                var renameCountReduced = 0

                if (local.rename_count == null) {
                    renameCountReduced = 0
                } else {
                    renameCountReduced = local.rename_count + 1
                }

                val response = pokemonRepository.renamePokemon(
                    RenamePokemonRequest(
                        name = local.name,
                        renameCount = renameCountReduced
                    )
                )
                renamePokemonResponse.value = Resource.Success(response)
                Log.d("renameResponse", response.toString())
                CoroutineScope(Dispatchers.IO).launch {
                    pokemonRepository.updatePokemon(
                        pokemonDetailLocal = PokemonDetailLocal(
                            id = local.id,
                            rename_count = renameCountReduced,
                            imageUrl = local.imageUrl,
                            name = response.pokemonName.replace(Regex("-\\d+"), "")
                        )
                    )
                }

            } catch (e: HttpException) {
                renamePokemonResponse.value = Resource.Error(e.toString())
            }catch (e: IOException) {
                renamePokemonResponse.value = Resource.Error(e.toString())
            }
        }
    }


}