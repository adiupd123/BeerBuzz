package com.adiupd123.beerbuzz.models.remote

data class Method(
    val fermentation: Fermentation,
    val mash_temp: List<MashTemp>,
    val twist: Any
)