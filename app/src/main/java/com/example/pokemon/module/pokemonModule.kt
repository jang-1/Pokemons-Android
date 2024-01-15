package com.example.pokemon.module

import com.example.pokemon.MainViewModel
import com.example.pokemon.data.PokemonService
import com.example.pokemon.details.DetailsViewModel
import com.example.pokemon.repository.PokemonRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pokemonModule = module {
    single { PokemonRepository()}
    viewModel {MainViewModel(get())}
    viewModel {DetailsViewModel(get())}
}