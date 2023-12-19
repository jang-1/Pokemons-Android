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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val pokemonService = PokemonService.retrofit.create(PokemonService::class.java)

    private suspend fun getPokemonResponse(): Response<PokemonResponse> = pokemonService.getPokemonResponse()
    val mutablePokemonData = MutableLiveData<UiState<List<Pokemon>>>()
    val immutablePokemonData: LiveData<UiState<List<Pokemon>>> = mutablePokemonData

    private fun getStatValue(stats: List<StatResponse>?, statName: String): Int {
        return stats?.find { it.stat.name == statName }?.base_stat ?: 0
    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var pokemons = getPokemonResponse().body()?.results
                pokemons?.forEachIndexed { index, pokemon ->
                    val detailsResponse = pokemonService.getPokemonDetailsResponse(index + 1).body()
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

    fun getPokemonDetailsByName(pokemonName: String): PokemonDetail? {
        val pokemon = immutablePokemonData.value?.data?.find { it.name == pokemonName }
        return pokemon?.details
    }
}
