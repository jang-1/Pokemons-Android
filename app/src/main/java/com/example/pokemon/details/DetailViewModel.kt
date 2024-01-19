package com.example.pokemon.details

import UiState
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.MainViewModel
import com.example.pokemon.data.PokemonDetail
import com.example.pokemon.data.PokemonService
import com.example.pokemon.data.StatResponse
import com.example.pokemon.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(private val pokemonRepository: PokemonRepository) : ViewModel() {


    private val _pokemonDetail = MutableLiveData<UiState<PokemonDetail?>>()
    val pokemonDetail: LiveData<UiState<PokemonDetail?>> get() = _pokemonDetail

    private fun getStatValue(stats: List<StatResponse>?, statName: String): Int {
        return stats?.find { it.stat.name == statName }?.base_stat ?: 0
    }

    suspend fun loadPokemonDetail(pokemonName: String) {
        _pokemonDetail.value = UiState(isLoading = true)
//        _pokemonDetail.postValue(UiState( isLoading = true))

        try {
             withContext(Dispatchers.IO) {
                val detailsResponse = pokemonRepository.getPokemonDetailsByName(pokemonName).body()
                val details = PokemonDetail(
                    height = detailsResponse?.height ?: 1.0,
                    weight = detailsResponse?.weight ?: 1.0,
                    hp = getStatValue(detailsResponse?.stats, "hp"),
                    attack = getStatValue(detailsResponse?.stats, "attack"),
                    defense = getStatValue(detailsResponse?.stats, "defense"),
                    speed = getStatValue(detailsResponse?.stats, "speed"),
                    frontImage = detailsResponse?.sprites?.frontDefault.orEmpty()
                )
                _pokemonDetail.postValue(UiState(data = details, isLoading = false))
            }

        } catch (e: Exception) {
            _pokemonDetail.value = UiState(error = "Operacja nie powiodła się", isLoading = false)
        }
    }
}
