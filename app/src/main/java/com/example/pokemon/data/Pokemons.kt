package com.example.pokemon.data

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    val count: Int,
    val results: List<Pokemon>
)
data class Pokemon(
    val name: String,
    var details: PokemonDetail = PokemonDetail(0, 0, 0, 0, 0, 0, "")
)

data class PokemonDetail(
    val height: Number,
    val weight: Number,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val frontImage: String
)

data class PokemonDetailResponse(
    val height: Number,
    val weight: Number,
    val stats: List<StatResponse>,
    val sprites: SpritesResponse
)

data class SpritesResponse(
    @SerializedName("front_default")
    val frontDefault: String
)

data class StatResponse(
    val base_stat: Int,
    val stat: StatInfoResponse
)

data class StatInfoResponse(
    val name: String
)

