package com.adiupd123.beerbuzz.models.remote

data class Ingredients(
    val hops: List<Hop>,
    val malt: List<Malt>,
    val yeast: String
)