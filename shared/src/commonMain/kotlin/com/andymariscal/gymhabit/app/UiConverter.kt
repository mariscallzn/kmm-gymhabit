package com.andymariscal.gymhabit.app

import com.andymariscal.gymhabit.data.model.FullExercise
import com.andymariscal.gymhabit.data.model.FullRoutinePlan
import com.andymariscal.gymhabit.data.model.FullRoutinePlanExercises

fun convert(fullRoutinePlan: FullRoutinePlan): UiRoutinePlan =
    UiRoutinePlan(
        fullRoutinePlan.id,
        fullRoutinePlan.name,
        convert(fullRoutinePlan.routinePlanExercises)
    )

fun convert(fullRoutinePlanExercises: FullRoutinePlanExercises): UiRoutinePlanExercise =
    UiRoutinePlanExercise(
        fullRoutinePlanExercises.id,
        fullRoutinePlanExercises.exercise.map(::convert)
    )

fun convert(fullExercise: FullExercise): UiExercise =
    UiExercise(
        fullExercise.exercise.id,
        fullExercise.exercise.name,
        fullExercise.muscles.map { uiM -> UiMuscles(uiM.id, uiM.name) },
        fullExercise.equipments.map { uiEq -> UiEquipment(uiEq.id, uiEq.name) })
