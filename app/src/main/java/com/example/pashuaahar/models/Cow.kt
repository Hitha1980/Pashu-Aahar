package com.example.pashuaahar.models

data class Cow(
    val name: String,
    val breed: String,
    val weight: Int,
    val age: Int,
    val milkYield: Double,
    val imageUri: String? = null
)