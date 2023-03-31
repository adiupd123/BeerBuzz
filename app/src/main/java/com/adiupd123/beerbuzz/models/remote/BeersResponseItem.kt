package com.adiupd123.beerbuzz.models.remote

data class BeersResponseItem(
    val abv: Double,
    val attenuation_level: Int,
    val boil_volume: BoilVolume,
    val brewers_tips: String,
    val contributed_by: String,
    val description: String,
    val ebc: Int,
    val first_brewed: String,
    val food_pairing: List<String>,
    val ibu: Double,
    val id: Int,
    val image_url: String,
    val ingredients: Ingredients,
    val method: Method,
    val name: String,
    val ph: Double,
    val srm: Int,
    val tagline: String,
    val target_fg: Int,
    val target_og: Double,
    val volume: Volume
)