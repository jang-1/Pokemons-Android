package com.example.pokemon.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {

    @GET("/api/v2/pokemon")
    suspend fun getPokemonResponse(): Response<PokemonResponse>

    @GET("/api/v2/pokemon/{id}")
    suspend fun getPokemonDetailsResponse(@Path("id") id: Int): Response<PokemonDetailResponse>

    @GET("/api/v2/pokemon/{name}")
    suspend fun getPokemonDetailsByName(@Path("name") name: String): Response<PokemonDetailResponse>

    companion object {
        private const val POKEMON_URL = "https://pokeapi.co/"

         val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(POKEMON_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val pokemonService: PokemonService by lazy {
            retrofit.create(PokemonService::class.java)
        }
    }



}