package com.andymariscal.gymhabit.data.model

data class FullRoutinePlanExercises(
    val id: Long,
    val exercisesSets: Map<FullExercise, List<FullSetPlan>>
)

data class FullSetPlan(
    val id: Long,
    val reps: Long,
    val weight: Float,
    val weightUnit: String
)