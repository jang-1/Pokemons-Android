package com.example.pokemon.repository
import com.example.pokemon.data.Pokemon
import com.example.pokemon.data.PokemonDetail
import com.example.pokemon.data.PokemonDetailResponse
import com.example.pokemon.data.PokemonResponse
import com.example.pokemon.data.PokemonService
import com.example.pokemon.data.StatResponse
import retrofit2.Response


class PokemonRepository {

    suspend fun getPokemonDetailsByName(name: String): Response<PokemonDetailResponse> = PokemonService.pokemonService.getPokemonDetailsByName(name)

    suspend fun getPokemonDetailsResponse(id: Int): Response<PokemonDetailResponse> = PokemonService.pokemonService.getPokemonDetailsResponse(id)

    suspend fun getPokemonResponse(): Response<PokemonResponse> = PokemonService.pokemonService.getPokemonResponse()

}