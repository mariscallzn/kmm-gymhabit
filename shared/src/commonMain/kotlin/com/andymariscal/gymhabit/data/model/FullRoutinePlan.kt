package com.andymariscal.gymhabit.data.model

data class FullRoutinePlan(
    val id: Long,
    val name: String,
    val routinePlanExercises: List<FullRoutinePlanExercises>
)