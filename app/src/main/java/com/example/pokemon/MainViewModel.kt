package com.example.pokemon

import UiState
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.data.Pokemon
import com.example.pokemon.data.PokemonDetail
import com.example.pokemon.data.PokemonResponse
import com.example.pokemon.data.PokemonService
import com.example.pokemon.data.StatResponse
import com.example.pokemon.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val pokemonRepository: PokemonRepository) : ViewModel() {


    private suspend fun getPokemonResponse(): Response<PokemonResponse> = pokemonRepository.getPokemonResponse()
    val mutablePokemonData = MutableLiveData<UiState<List<Pokemon>>>()
    val immutablePokemonData: LiveData<UiState<List<Pokemon>>> = mutablePokemonData

    private fun getStatValue(stats: List<StatResponse>?, statName: String): Int {
        return stats?.find { it.stat.name == statName }?.base_stat ?: 0
    }

    val filterQuery = MutableLiveData("")
    fun updateFilterQuery(text: String){
        filterQuery.postValue(text)
    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mutablePokemonData.postValue(UiState( isLoading = true))
                var pokemons = getPokemonResponse().body()?.results
                pokemons?.forEachIndexed { index, pokemon ->
                    val detailsResponse = pokemonRepository.getPokemonDetailsResponse(index + 1).body()
                    val details = PokemonDetail(
                        height = detailsResponse?.height ?: 1.0,
                        weight = detailsResponse?.weight ?: 1.0,
                        hp = getStatValue(detailsResponse?.stats, "hp"),
                        attack = getStatValue(detailsResponse?.stats, "attack"),
                        defense = getStatValue(detailsResponse?.stats, "defense"),
                        speed = getStatValue(detailsResponse?.stats, "speed"),
                        frontImage = detailsResponse?.sprites?.frontDefault.orEmpty()
                    )
                    pokemon.details = details
                }

                mutablePokemonData.postValue(UiState(data = pokemons, isLoading = false))
            } catch (e: Exception) {
                mutablePokemonData.postValue(UiState(error = "Operacja nie powiodła się", isLoading = false))
                Log.e("MainViewModel", "Operacja nie powiodła się", e)
            }
        }
    }

}
