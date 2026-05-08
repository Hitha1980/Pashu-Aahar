package com.example.pashuaahar.utils

data class FeedPlan(
    val maize: Int,
    val cake: Int,
    val cost: Int,
    val protein: Int,
    val energy: Int
)

object SmartFeedEngine {

    fun generate(breed: String, weight: Int, milk: Int): FeedPlan {

        val protein = (50 * milk) + (2 * weight)
        val energy = (70 * milk) + (3 * weight)

        val maize = (energy / 120).coerceAtLeast(2)
        val cake = (protein / 150).coerceAtLeast(1)

        val cost = maize * 25 + cake * 40

        return FeedPlan(maize, cake, cost, protein, energy)
    }
}
