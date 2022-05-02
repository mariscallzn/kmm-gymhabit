package com.andymariscal.gymhabit.data.model

import db.Equipment
import db.Exercise
import db.Muscle

data class FullExercise (
    val exercise: Exercise,
    val muscles: List<Muscle>,
    val equipments: List<Equipment>
)